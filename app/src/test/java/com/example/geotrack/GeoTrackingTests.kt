package com.example.geotrack

import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.routeTracking.GeoRepository
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.ui.tracking.state.TrackingIntent
import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import com.example.geotrack.util.GpxConverter
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.osmdroid.util.GeoPoint

class GeoTrackingTests {
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    @Test
    fun `abandonTracking should reset state`() = runTest {
        val viewModel = createTrackingViewModel()

        viewModel.processIntent(TrackingIntent.AbandonTracking)

        val state = viewModel.state.value
        assertFalse(state.isTracking)
        assertEquals("0 км", state.totalDistance)
        assertEquals("0.0 км/ч", state.currentSpeed)
        assertEquals("0:00", state.elapsedTime)
    }

    @Test
    fun `updateLocation should add point when not paused`() = runTest {
        val viewModel = createTrackingViewModel()

        viewModel.processIntent(TrackingIntent.UpdateLocation(GeoPoint(55.0, 37.0), 2.5f))

        val state = viewModel.state.value
        assertEquals(1, state.geoPoints.size)
        assertEquals("9.0 км/ч", state.currentSpeed)
    }
    @Test
    fun `togglePause should switch paused state`() = runTest {
        val viewModel = createTrackingViewModel()

        viewModel.processIntent(TrackingIntent.TogglePause)
        assertTrue(viewModel.state.value.isPaused)

        viewModel.processIntent(TrackingIntent.TogglePause)
        assertFalse(viewModel.state.value.isPaused)
    }
    fun `updateRouteName should update state`() = runTest {
        val viewModel = createTrackingViewModel()

        viewModel.processIntent(TrackingIntent.UpdateRouteName("Мой маршрут"))

        assertEquals("Мой маршрут", viewModel.state.value.routeName)
    }

    private fun createTrackingViewModel(): TrackingViewModel {
        val repo = mockk<GeoRepository>(relaxed = true)
        val interactor = mockk<TrackInteractor>(relaxed = true)
        val converter = mockk<GpxConverter>(relaxed = true)
        val profile = mockk<UserProfileInteractor>(relaxed = true)
        return TrackingViewModel(repo, interactor, converter, profile)
    }


}

