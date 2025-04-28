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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.example.geotrack.R
import com.example.geotrack.domain.Route
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.user_profile.viewModel.HistoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun ProfileScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    onTrackSelected: (Long) -> Unit = { }
) {
    val state by viewModel.state.collectAsState()
    var profileImageUrl: String by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var routesCompleted: Int by remember { mutableIntStateOf(0) }
//    var routes: MutableList<Route> = remember {
//        mutableStateListOf<Route>(
//            Route(
//                1,
//                "От туда в туда",
//                "10 декабря 2024",
//                10.0F,
//                100.0F,
//                "2:15:40",
//                99,
//                null
//            )
//        )
//    }
    Scaffold(
        topBar = { ScreenHeader(stringResource(R.string.Profile), Modifier.padding(start = 16.dp)) }
    ) { paddingValues ->
        ConstraintLayout(modifier = Modifier.padding(paddingValues)) {
            val (profileImage, userName, routesCompletedHeader, routesCompletedValue, spacer, routesListHeader, routesList) = createRefs()

            GlideImage(
                model = profileImageUrl,
                contentDescription = "Profile image",
                failure = placeholder(R.drawable.avatar_placeholder),
                modifier = Modifier
                    .constrainAs(profileImage) {
                        top.linkTo(parent.top)
                    }
                    .height(120.dp)
                    .width(120.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
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
                text = routesCompleted.toString(),
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
            TrackList(tracks = state.tracks, onTrackClick = onTrackSelected, onDeleteTrack = {viewModel.deleteTrack(it)},
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

@OptIn(ExperimentalGlideComposeApi::class)
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
        GlideImage(
            model = route.image,
            contentDescription = "Route image",
            failure = placeholder(R.drawable.map_placeholder),
            modifier = Modifier
                .constrainAs(routeImage) {
                    top.linkTo(parent.top, margin = 14.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .height(64.dp)
                .width(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
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

