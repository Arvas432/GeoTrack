package com.example.geotrack.ui.user_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.geotrack.R
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.user_profile.viewModel.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun ProfileScreen(
    viewModel: UserProfileViewModel = koinViewModel(),
    onTrackSelected: (Long) -> Unit = { }
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            ScreenHeader(
                stringResource(R.string.Profile),
                Modifier
                    .padding(start = 16.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            val (profileImage, userName, routesCompletedHeader, routesCompletedValue, spacer, routesListHeader, routesList) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(profileImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                state.profileImageBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Profile image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: Icon(
                    imageVector = Icons.Default.AccountCircle,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Default profile",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = state.name,
                fontFamily = robotoFamily,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.constrainAs(userName) {
                    top.linkTo(profileImage.top)
                    start.linkTo(profileImage.end, margin = 14.dp)
                }
            )
            Text(
                text = stringResource(R.string.routes_completed),
                fontFamily = robotoFamily,
                fontSize = 12.sp,
                color = GrayB4,
                modifier = Modifier.constrainAs(routesCompletedHeader) {
                    top.linkTo(userName.bottom)
                    start.linkTo(profileImage.end, margin = 14.dp)
                }
            )
            Text(
                text = state.completedRoutes.toString(),
                fontFamily = robotoFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.constrainAs(routesCompletedValue) {
                    top.linkTo(userName.bottom)
                    start.linkTo(routesCompletedHeader.end)
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GrayB4)
                    .constrainAs(spacer) {
                        top.linkTo(profileImage.bottom, margin = 24.dp)
                    }
            )
            Text(
                text = stringResource(R.string.saved_routes),
                fontFamily = robotoFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.constrainAs(routesListHeader) {
                    top.linkTo(spacer.bottom, margin = 6.dp)
                    start.linkTo(parent.start, margin = 14.dp)
                }
            )
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            TrackList(tracks = state.tracks,
                onTrackClick = onTrackSelected,
                onDeleteTrack = { viewModel.deleteTrack(it) },
                modifier = Modifier
                    .constrainAs(routesList) {
                        top.linkTo(routesListHeader.bottom, margin = 4.dp)
                    }
                    .fillMaxSize())
        }
    }
}


@Composable
private fun TrackList(
    tracks: List<Track>,
    onTrackClick: (Long) -> Unit,
    onDeleteTrack: (Long) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = tracks,
            key = { track -> track.id!! }
        ) { track ->
            RouteListItem(track, onTrackClick, onDeleteTrack)
        }
    }
}


@Composable
fun RouteListItem(
    route: Track,
    onTrackClick: (Long) -> Unit,
    onDeleteTrack: (Long) -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .height(150.dp)
        .clickable {
            if (route.id != null) {
                onTrackClick(route.id)
            }
        }) {
        val (routeTitle,
            routeDate,
            routeParameters,
            routeImage) = createRefs()
        Text(text = route.name,
            fontFamily = robotoFamily,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.constrainAs(routeTitle) {
                top.linkTo(parent.top, margin = 4.dp)
                start.linkTo(parent.start, margin = 8.dp)
            }
        )
        Text(
            text = route.date.toString(),
            fontFamily = robotoFamily,
            fontSize = 10.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = GrayB4,
            modifier = Modifier.constrainAs(routeDate) {
                top.linkTo(routeTitle.bottom, margin = 4.dp)
                start.linkTo(parent.start, margin = 8.dp)
            }
        )
        if ((route.image) != null) {
            Image(
                painter = BitmapPainter(route.image.asImageBitmap()),
                contentDescription = "Route image",
                modifier = Modifier
                    .constrainAs(routeImage) {
                        top.linkTo(parent.top, margin = 14.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .height(64.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "Route image",
                modifier = Modifier
                    .constrainAs(routeImage) {
                        top.linkTo(parent.top, margin = 14.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .height(64.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .constrainAs(routeParameters) {
                    bottom.linkTo(parent.bottom)
                }) {
            ValueWithHeader(stringResource(R.string.avg_speed), buildString {
                append(Math.round(route.averageSpeed * 10.0) / 10.0)
                append(
                    stringResource(
                        R.string.kmh
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(stringResource(R.string.distance), buildString {
                append(Math.round(route.distance * 10.0) / 10.0)
                append(
                    stringResource(
                        R.string.km
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(
                stringResource(R.string.time),
                buildString {
                    append(route.duration.inWholeMinutes.toString())
                    append(stringResource(R.string.mins))
                },
                Modifier.weight(1F)
            )
            ValueWithHeader(
                stringResource(R.string.likes),
                route.likes.toString(),
                Modifier.weight(1F)
            )
        }
    }
}

