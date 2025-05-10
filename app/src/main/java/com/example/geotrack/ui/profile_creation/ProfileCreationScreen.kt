package com.example.geotrack.ui.profile_creation

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.geotrack.R
import com.example.geotrack.ui.common_ui_components.Add_photo
import com.example.geotrack.ui.common_ui_components.DefaultButton
import com.example.geotrack.ui.common_ui_components.HintedLabellessTextField
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import com.example.geotrack.ui.profile_creation.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileCreation(
    viewModel: ProfileViewModel = koinViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.setProfileImage(it)
            viewModel.setProfileImage(it)
            scope.launch(Dispatchers.IO) {
                loadImageBitmap(context.contentResolver, it)?.let { bmp ->
                    bitmap.value = bmp.asAndroidBitmap()
                }
            }
        }
    }

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
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (image_picker, name_field, height_field, weight_field, create_button) = createRefs()
            Box(
                modifier = Modifier
                    .padding(top = 55.dp, bottom = 37.dp)
                    .size(200.dp)
                    .constrainAs(image_picker) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(name_field.top)
                    }.clip(CircleShape)
                        .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
            ) {
                val imageModifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CircleShape
                    )
                if (state.profileImageUri != null) {
                    bitmap.value?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = stringResource(R.string.profile_image),
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                    } ?: CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Image(
                        imageVector = Add_photo,
                        contentDescription = stringResource(R.string.add_image),
                        modifier = imageModifier,
                        contentScale = ContentScale.Inside
                    )
                }
            }
            HintedLabellessTextField(
                text = state.name,
                hintText = stringResource(R.string.your_name),
                onTextChanged = {viewModel.setName(it)},
                modifier = Modifier
                    .padding(start = 17.dp, end = 15.dp, bottom = 17.dp)
                    .constrainAs(name_field) {
                        top.linkTo(image_picker.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(height_field.top)
                    }
            )
            HintedLabellessTextField(
                text = state.height,
                hintText = stringResource(R.string.your_height),
                onTextChanged = {viewModel.setHeight(it)},
                modifier = Modifier
                    .padding(start = 17.dp, end = 15.dp, bottom = 17.dp)
                    .constrainAs(height_field) {
                        top.linkTo(name_field.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(weight_field.top)
                    }
            )
            HintedLabellessTextField(
                text = state.weight,
                hintText = stringResource(R.string.your_weight),
                onTextChanged = {viewModel.setWeight(it)},
                modifier = Modifier
                    .padding(start = 17.dp, end = 15.dp, bottom = 17.dp)
                    .constrainAs(weight_field) {
                        top.linkTo(height_field.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            DefaultButton(
                text = stringResource(R.string.create_profile),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .constrainAs(create_button) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    viewModel.saveProfile()
                    navController.navigate("tracking_route") {
                        popUpTo("profile_creation_route") { inclusive = true }
                    }
                }
            )
        }
    }
}

private suspend fun loadImageBitmap(
    contentResolver: ContentResolver,
    uri: Uri
): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val size = Size(512, 512)
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.loadThumbnail(uri, size, null)
        } else {
            contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        }
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

