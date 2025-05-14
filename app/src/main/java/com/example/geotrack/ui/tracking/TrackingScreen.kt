package com.example.geotrack.ui.tracking

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.geotrack.R
import com.example.geotrack.ui.common_ui_components.Abandon
import com.example.geotrack.ui.common_ui_components.Pause
import com.example.geotrack.ui.common_ui_components.Stop
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.example.geotrack.ui.common_ui_components.DefaultButton
import com.example.geotrack.ui.common_ui_components.HintedLabellessTextField
import com.example.geotrack.ui.tracking.state.TrackingIntent
import com.example.geotrack.util.MapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TrackingScreen(viewModel: TrackingViewModel = koinViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val lineColor = MaterialTheme.colorScheme.secondary.toArgb()
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) viewModel.processIntent(TrackingIntent.StartTracking)
    }
    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        topBar = {
            RouteParameters(
                state.currentSpeed,
                state.totalDistance,
                state.elapsedTime,
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        },
    ) { paddingValues ->
        var mapView by remember { mutableStateOf<MapView?>(null) }
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            val (map, stopButton, pauseButton, abandonButton) = createRefs()

            AndroidView(
                modifier = Modifier.constrainAs(map) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(
                            XYTileSource(
                                "HttpMapnik",
                                0, 19, 256, ".png", arrayOf(
                                    "https://a.tile.openstreetmap.org/",
                                    "https://b.tile.openstreetmap.org/",
                                    "https://c.tile.openstreetmap.org/"
                                ),
                                "© OpenStreetMap contributors"
                            )
                        )
                        isHorizontalMapRepetitionEnabled = false
                        isVerticalMapRepetitionEnabled = false
                        maxZoomLevel = 20.0
                        minZoomLevel = 4.0
                        controller.setZoom(10.0)
                        setMultiTouchControls(true)
                    }.also { mapView = it }
                },
                update = { view ->
                    state.geoPoints.lastOrNull()?.let {
                        view.controller.animateTo(it)
                    }

                    view.overlays.removeAll { it is Polyline }
                    val polyline = Polyline().apply {
                        setPoints(ArrayList(state.geoPoints))
                        outlinePaint.color = lineColor
                        outlinePaint.strokeWidth = 12f
                    }
                    view.overlays.add(polyline)
                    view.invalidate()
                }
            )
            DisposableEffect(Unit) {
                mapView?.onResume()
                onDispose { mapView?.onPause() }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .size(100.dp)
                    .padding(bottom = 10.dp)
                    .constrainAs(pauseButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        if (!state.isTracking) {
                            viewModel.processIntent(TrackingIntent.StartTracking)
                        } else {
                            viewModel.processIntent(TrackingIntent.TogglePause)
                        }

                    }) {
                Icon(
                    imageVector = if (!state.isPaused && state.isTracking) {
                        Pause
                    } else if (!state.isTracking) {
                        Icons.Filled.PlayArrow
                    } else {
                        Icons.Filled.PlayArrow
                    },
                    contentDescription = "Tracking controls, pause",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(bottom = 25.dp)
                    .constrainAs(stopButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(pauseButton.start)
                    }
                    .clickable {
                        if (state.isTracking || state.isPaused) {
                            viewModel.processIntent(TrackingIntent.ToggleTrackingEndMenu)
                            showBottomSheet = true
                        }
                    }) {
                Icon(
                    imageVector = Stop,
                    contentDescription = "Tracking controls, finish",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .size(70.dp)
                    .padding(bottom = 25.dp)
                    .constrainAs(abandonButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(pauseButton.end)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        viewModel.processIntent(TrackingIntent.AbandonTracking)
                    }) {
                Icon(
                    imageVector = Abandon,
                    contentDescription = "Tracking controls, abandon",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

        }
        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = {
                    showBottomSheet = false
                },
                content = {
                    HintedLabellessTextField(
                        state.routeName,
                        stringResource(R.string.route_name),
                        onTextChanged = {
                            viewModel.processIntent(TrackingIntent.UpdateRouteName(it))
                        },
                        Modifier.padding(start = 14.dp, end = 18.dp, top = 16.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 20.dp)
                    ) {
                        ValueWithHeader(
                            stringResource(R.string.avg_speed),
                            state.avgSpeed,
                            Modifier.weight(1F)
                        )
                        ValueWithHeader(
                            stringResource(R.string.distance),
                            state.totalDistance,
                            Modifier.weight(1F)
                        )
                        ValueWithHeader(
                            stringResource(R.string.time),
                            state.elapsedTime,
                            Modifier.weight(1F)
                        )
                        ValueWithHeader(
                            stringResource(R.string.calories),
                            state.calories,
                            Modifier.weight(1F)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        DefaultButton(
                            text = stringResource(R.string.delete),
                            modifier = Modifier.weight(1F)
                        ) {
                            viewModel.processIntent(TrackingIntent.AbandonTracking)
                            showBottomSheet = false
                        }
                        DefaultButton(
                            text = stringResource(R.string.save), modifier = Modifier
                                .weight(1F)
                                .padding(start = 10.dp)
                        ) {
                            mapView?.let {
                                lifecycleOwner.lifecycleScope.launch {
                                    val snapshot = createMapSnapshot(it, state.geoPoints, lineColor)
                                    viewModel.processIntent(
                                        TrackingIntent.StopTracking(
                                            snapshot ?: AppCompatResources
                                                .getDrawable(
                                                    context,
                                                    R.drawable.placeholder
                                                )
                                                ?.toBitmap(640, 480)
                                        )
                                    )
                                }
                            }
                            showBottomSheet = false
                        }
                    }
                }
            )
        }

    }
}

@Composable
fun RouteParameters(speed: String, distance: String, time: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(top = 10.dp)

    ) {
        ValueWithHeader(
            stringResource(R.string.speed),
            speed,
            Modifier
                .weight(1F)
                .padding(start = 5.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(GrayB4)
        )
        ValueWithHeader(
            stringResource(R.string.distance),
            distance,
            Modifier
                .weight(1F)
                .padding(start = 5.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(GrayB4)
        )
        ValueWithHeader(
            stringResource(R.string.time),
            time,
            Modifier
                .weight(1F)
                .padding(start = 5.dp)
        )
    }
}


suspend fun createMapSnapshot(
    mapView: MapView,
    geoPoints: List<GeoPoint>,
    lineColor: Int
): Bitmap? = withContext(Dispatchers.Main) {
    if (geoPoints.isEmpty() || mapView.width == 0 || mapView.height == 0) return@withContext null

    val boundingBox = MapUtils.createSquareBoundingBox(geoPoints)
    val bufferPixels = 100
    mapView.zoomToBoundingBox(boundingBox, true)
    delay(500)
    val bitmap = Bitmap.createBitmap(
        min(mapView.width, mapView.height) + bufferPixels,
        min(mapView.width, mapView.height) + bufferPixels,
        Bitmap.Config.ARGB_8888
    ).also {
        val canvas = Canvas(it)
        mapView.draw(canvas)
        val projection = mapView.projection
        val path = Path().apply {
            geoPoints.forEachIndexed { i, point ->
                val pixelPoint = Point().apply {
                    projection.toPixels(point, this)
                }
                if (i == 0) moveTo(pixelPoint.x.toFloat(), pixelPoint.y.toFloat())
                else lineTo(pixelPoint.x.toFloat(), pixelPoint.y.toFloat())
            }
        }

        Paint().apply {
            color = lineColor
            strokeWidth = 12f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }.let { paint ->
            canvas.drawPath(path, paint)
        }
    }

    bitmap
}
