package com.example.geotrack

import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.profile.model.UserProfile
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.ui.user_profile.viewModel.UserProfileViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import java.time.Instant
import kotlin.time.Duration
@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTests {
    private val trackRepository = mockk<TrackRepository>()
    private val userProfileInteractor = mockk<UserProfileInteractor>()

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var viewModel: UserProfileViewModel

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load profile data on init`() = runTest {
        val profile = UserProfile(name = "Artem", height = 0, weight = 0, profileImageBitmap = null, completedRoutes = 5)
        coEvery { userProfileInteractor.getProfile() } returns profile
        coEvery { trackRepository.getAllTracks() } returns emptyFlow()

        viewModel = UserProfileViewModel(trackRepository, userProfileInteractor)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Artem", state.name)
        assertEquals(5, state.completedRoutes)
    }

    @Test
    fun `should load and sort tracks`() = runTest {
        val profile = UserProfile(name = "User", height = 0, weight = 0, profileImageBitmap = null, completedRoutes = 0)
        coEvery { userProfileInteractor.getProfile() } returns profile

        val track1 = Track(localDbId = 1, name = "track", date = Instant.ofEpochMilli(100000000000), duration = Duration.ZERO, distance = 0.0, averageSpeed = 0.0, gpxData = "", likes = 0, image = null)
        val track2 = Track(localDbId = 2, name = "track", date = Instant.ofEpochMilli(1000000000001), duration = Duration.ZERO, distance = 0.0, averageSpeed = 0.0, gpxData = "", likes = 0, image = null)
        val flow = MutableSharedFlow<List<Track>>(replay = 1)
        flow.tryEmit(listOf(track1, track2))

        coEvery { trackRepository.getAllTracks() } returns flow

        viewModel = UserProfileViewModel(trackRepository, userProfileInteractor)

        advanceUntilIdle()

        val tracks = viewModel.state.value.tracks
        assertEquals(listOf(track2, track1), tracks) // sorted descending
    }

    @Test
    fun `should update error on tracks loading failure`() = runTest {
        coEvery { userProfileInteractor.getProfile() } returns null
        coEvery { trackRepository.getAllTracks() } throws RuntimeException("Test error")

        viewModel = UserProfileViewModel(trackRepository, userProfileInteractor)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Test error", state.error)
    }

    @Test
    fun `should call deleteTrack without error`() = runTest {
        coEvery { userProfileInteractor.getProfile() } returns null
        coEvery { trackRepository.getAllTracks() } returns emptyFlow()
        coEvery { trackRepository.deleteTrack(1L) } just Runs

        viewModel = UserProfileViewModel(trackRepository, userProfileInteractor)

        viewModel.deleteTrack(1L)

        advanceUntilIdle()

        //verify { trackRepository.deleteTrack(1L) }
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `should update error if deleteTrack throws`() = runTest {
        coEvery { userProfileInteractor.getProfile() } returns null
        coEvery { trackRepository.getAllTracks() } returns emptyFlow()
        coEvery { trackRepository.deleteTrack(1L) } throws RuntimeException("Deletion failed")

        viewModel = UserProfileViewModel(trackRepository, userProfileInteractor)

        viewModel.deleteTrack(1L)

        advanceUntilIdle()

        val error = viewModel.state.value.error
        assertEquals("Не удалось удалить маршрут: Deletion failed", error)
    }
}