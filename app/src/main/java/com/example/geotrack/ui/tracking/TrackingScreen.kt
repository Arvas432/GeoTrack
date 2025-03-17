package com.example.geotrack.ui.tracking

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Looper
import android.util.Log
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
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geotrack.R
import com.example.geotrack.ui.common_ui_components.Abandon
import com.example.geotrack.ui.common_ui_components.Pause
import com.example.geotrack.ui.common_ui_components.Stop
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.theme.GrayEB
import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun TrackingScreen() {
    val viewModel: TrackingViewModel = koinViewModel()
    val context = LocalContext.current
    val state: TrackingState by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.processEvent(TrackingEvent.PermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.processEvent(TrackingEvent.PermissionResult(true))
        } else {
            Log.i("LAUNCHED EFFECT", "start tracking")
            viewModel.processEvent(TrackingEvent.StartTracking)
        }
        viewModel.processEvent(TrackingEvent.StartTracking)
    }

    when (val currentState = state) {
        is TrackingState.Tracking -> TrackingContent(
            state = currentState,
            onPauseResume = { viewModel.processEvent(TrackingEvent.TogglePause) },
            onStop = { viewModel.processEvent(TrackingEvent.StopTracking) },
            onAbandon = { viewModel.processEvent(TrackingEvent.AbandonTracking) }
        )

        is TrackingState.Error -> ErrorState(message = currentState.message)
        TrackingState.Idle -> IdleState { viewModel.processEvent(TrackingEvent.StartTracking) }
        TrackingState.RequestingPermission -> {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            LoadingState()
        }
    }
    Log.i("Current state", state.toString())
}
//
//
//    Scaffold(
//        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
//        topBar = {
//            RouteParameters(
//                "0.0 км/ч",
//                "0 км",
//                "0:00",
//                Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.primary)
//            )
//        }
//    ) { paddingValues ->
//        var geoPoint by remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
//        ConstraintLayout(modifier = Modifier.padding(paddingValues)) {
//            val (map, stopButton, pauseButton, abandonButton) = createRefs()
//            var mapView by remember { mutableStateOf<MapView?>(null) }
//
//            AndroidView(
//                modifier = Modifier.constrainAs(map) {
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                    end.linkTo(parent.end)
//                    start.linkTo(parent.start)
//                },
//                factory = { context ->
//                    MapView(context).apply {
//                        setTileSource(
//                            XYTileSource(
//                                "HttpMapnik",
//                                0, 19, 256, ".png", arrayOf(
//                                    "http://a.tile.openstreetmap.org/",
//                                    "http://b.tile.openstreetmap.org/",
//                                    "http://c.tile.openstreetmap.org/"
//                                ),
//                                "© OpenStreetMap contributors"
//                            )
//                        )
//                        isHorizontalMapRepetitionEnabled = false
//                        isVerticalMapRepetitionEnabled = false
//                        maxZoomLevel = 20.0
//                        minZoomLevel = 4.0
//                        controller.setZoom(10.0)
//                        setMultiTouchControls(true)
//                    }.also { mapView = it }
//                },
//                update = { view ->
//                    viewModel.geoPoints.lastOrNull()?.let {
//                        view.controller.animateTo(it)
//                    }
//
//                    view.overlays.removeAll { it is Polyline }
//                    val polyline = Polyline().apply {
//                        setPoints(ArrayList(viewModel.geoPoints))
//                        outlinePaint.color = lineColor
//                        outlinePaint.strokeWidth = 12f
//                    }
//                    view.overlays.add(polyline)
//                    view.invalidate()
//                }
//            )
//            DisposableEffect(Unit) {
//                mapView?.onResume()
//                onDispose { mapView?.onPause() }
//            }
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.secondary)
//                    .size(100.dp)
//                    .padding(bottom = 10.dp)
//                    .constrainAs(pauseButton) {
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .clickable {
//
//                    }) {
//                Icon(
//                    imageVector = Pause,
//                    contentDescription = "Tracking controls, pause",
//                    tint = MaterialTheme.colorScheme.onSecondary
//                )
//            }
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(70.dp)
//                    .background(MaterialTheme.colorScheme.secondary)
//                    .padding(bottom = 25.dp)
//                    .constrainAs(stopButton) {
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(pauseButton.start)
//                    }
//                    .clickable {
//
//                    }) {
//                Icon(
//                    imageVector = Stop,
//                    contentDescription = "Tracking controls, finish",
//                    tint = MaterialTheme.colorScheme.onSecondary,
//                    modifier = Modifier.padding(top = 20.dp)
//                )
//            }
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.secondary)
//                    .size(70.dp)
//                    .padding(bottom = 25.dp)
//                    .constrainAs(abandonButton) {
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(pauseButton.end)
//                        end.linkTo(parent.end)
//                    }
//                    .clickable {
//
//                    }) {
//                Icon(
//                    imageVector = Abandon,
//                    contentDescription = "Tracking controls, abandon",
//                    tint = MaterialTheme.colorScheme.onSecondary,
//                    modifier = Modifier.padding(top = 20.dp)
//                )
//            }
//
//        }
//
//    }
//    if (hasLocationPermission) {
//        LocationTracker(viewModel)
//    }
//
//
//}

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

@Composable
private fun TrackingContent(
    state: TrackingState.Tracking,
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    onAbandon: () -> Unit
) {
    Scaffold(
        topBar = {
            RouteParameters(
                speed = state.speed,
                distance = state.distance,
                time = state.time
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            MapViewComponent(geoPoints = state.geoPoints)
            ControlsPanel(
                isPaused = state.isPaused,
                onPauseResume = onPauseResume,
                onStop = onStop,
                onAbandon = onAbandon
            )
        }
    }
}

@Composable
fun ControlsPanel(
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    onAbandon: () -> Unit,
    isPaused: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
        ) {
            val (pauseButton, stopButton, abandonButton) = createRefs()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .size(70.dp)
                    .constrainAs(pauseButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable { onPauseResume() }
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Pause,
                    contentDescription = if (isPaused) "Resume" else "Pause",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(30.dp)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .size(56.dp)
                    .constrainAs(stopButton) {
                        bottom.linkTo(pauseButton.top, margin = 16.dp)
                        start.linkTo(pauseButton.start)
                        end.linkTo(pauseButton.end)
                    }
                    .clickable { onStop() }
            ) {
                Icon(
                    imageVector = Stop,
                    contentDescription = "Stop",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .size(56.dp)
                    .constrainAs(abandonButton) {
                        bottom.linkTo(pauseButton.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 32.dp)
                    }
                    .clickable { onAbandon() }
            ) {
                Icon(
                    imageVector = Abandon,
                    contentDescription = "Abandon",
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MapViewComponent(geoPoints: List<GeoPoint>) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val lineColorCode = MaterialTheme.colorScheme.secondary.toArgb()

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setupBaseMap()
            }.also { mapView = it }
        },
        update = { view ->
            updateMapView(view, lineColorCode, geoPoints)
        }
    )

    DisposableEffect(Unit) {
        mapView?.onResume()
        onDispose {
            mapView?.onPause()
            mapView?.overlays?.clear()
        }
    }
}

private fun MapView.setupBaseMap() {
    setTileSource(
        XYTileSource(
            "HttpMapnik",
            0, 19, 256, ".png", arrayOf(
                "http://a.tile.openstreetmap.org/",
                "http://b.tile.openstreetmap.org/",
                "http://c.tile.openstreetmap.org/"
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
}

private fun updateMapView(mapView: MapView, lineColor: Int, geoPoints: List<GeoPoint>) {
    mapView.overlays.removeAll { it is Polyline }

    if (geoPoints.isNotEmpty()) {
        val polyline = Polyline().apply {
            setPoints(ArrayList(geoPoints))
            outlinePaint.color = lineColor
            outlinePaint.strokeWidth = 12f
        }
        mapView.overlays.add(polyline)
        mapView.controller.animateTo(geoPoints.last())
    }

    mapView.invalidate()
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun IdleState(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onStartClick) {
            Text("Начать трекинг")
        }
    }
}

