package com.example.geotrack.ui.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.geotrack.R
import com.example.geotrack.domain.Post
import com.example.geotrack.domain.Route
import com.example.geotrack.domain.User
import com.example.geotrack.ui.common_ui_components.HintedLabellessTextField
import com.example.geotrack.ui.common_ui_components.LikeIcon
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.theme.GrayB4
import com.example.geotrack.ui.user_profile.RouteListItem

@Preview
@Composable
fun RoutesScreen() {
    var feed: MutableList<Post> = remember {
        mutableStateListOf<Post>(
            Post(
                1,
                User(1, "GOOOOL", 23, null), Route(
                    1,
                    "GOOOOOOOL",
                    "10 декабря 2024",
                    10.0F,
                    100.0F,
                    "2:15:40",
                    99,
                    null
                )
            )
        )
    }
    Scaffold(
        topBar = {
            ScreenHeader(
                stringResource(R.string.routes_screen),
                Modifier.padding(start = 16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            HintedLabellessTextField(
                "",
                "Поиск",
                {},
                modifier = Modifier.padding(start = 17.dp, end = 15.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(feed, key = { post ->
                    post.id
                }) { post ->
                    PostListItem(post)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostListItem(post: Post) {
    ConstraintLayout(
        modifier = Modifier
            .height(350.dp)
            .padding(horizontal = 10.dp)
    ) {
        val (routeTitle,
            profilePicture,
            postParameters,
            postDate,
            postImage) = createRefs()
        GlideImage(
            model = post.user.profileImageUri,
            contentDescription = "Profile image",
            modifier = Modifier
                .constrainAs(profilePicture) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .height(42.dp)
                .width(42.dp)
                .clip(CircleShape)
        )
        Text(text = post.route.title,
            fontFamily = robotoFamily,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.constrainAs(routeTitle) {
                top.linkTo(profilePicture.top)
                start.linkTo(profilePicture.end, margin = 6.dp)
            }
        )
        Text(
            text = post.route.date,
            fontFamily = robotoFamily,
            fontSize = 10.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = GrayB4,
            modifier = Modifier.constrainAs(postDate) {
                top.linkTo(routeTitle.bottom)
                start.linkTo(routeTitle.start)
            }
        )
        GlideImage(
            model = post.route.imageUri,
            contentDescription = "Route image",
            modifier = Modifier
                .constrainAs(postImage) {
                    top.linkTo(parent.top, margin = 14.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(postParameters.top)
                }
        )
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(postParameters) {
                    bottom.linkTo(parent.bottom)
                }) {
            ValueWithHeader(stringResource(R.string.avg_speed), buildString {
                append(post.route.avgSpeed.toString())
                append(
                    stringResource(
                        R.string.kmh
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(stringResource(R.string.distance), buildString {
                append(post.route.distance.toString())
                append(
                    stringResource(
                        R.string.km
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(stringResource(R.string.time), post.route.time, Modifier.weight(1F))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable {

                    },
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.likes),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = GrayB4,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Row(
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Image(LikeIcon, "Like button")
                    Text(
                        text = post.route.likes.toString(),
                        fontSize = 12.sp,
                        lineHeight = 26.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Medium,
                        color = GrayB4,
                    )

                }

            }
        }
    }
}