package com.example.geotrack

import com.example.geotrack.domain.social.Post
import com.example.geotrack.domain.social.PostsInteractor
import com.example.geotrack.ui.social.viewmodel.RoutesIntent
import com.example.geotrack.ui.social.viewmodel.RoutesState
import com.example.geotrack.ui.social.viewmodel.RoutesViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.coJustRun
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.Instant
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class RoutesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var interactor: PostsInteractor
    private lateinit var viewModel: RoutesViewModel

    private val post1 = Post(
        serverId = 1L,
        username = "user1",
        name = "Route A",
        date = Instant.parse("2024-01-01T10:00:00Z"),
        duration = Duration.ZERO,
        distance = 5.0,
        averageSpeed = 10.0,
        gpxData = null,
        likes = 3,
        liked = false,
        image = null
    )

    private val post2 = Post(
        serverId = 2L,
        username = "user2",
        name = "Route B",
        date = Instant.parse("2024-01-02T10:00:00Z"),
        duration = Duration.ZERO,
        distance = 7.5,
        averageSpeed = 10.0,
        gpxData = null,
        likes = 5,
        liked = true,
        image = null
    )

    @Before
    fun setUp() {
        interactor = mockk()
    }

    @Test
    fun `should load posts successfully and update state to Loaded`() = runTest {
        // given
        coEvery { interactor.loadPosts() } returns listOf(post1, post2)

        // when
        viewModel = RoutesViewModel(interactor)
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is RoutesState.Loaded)
        assertEquals(listOf(post1, post2), (state as RoutesState.Loaded).posts)
    }

    @Test
    fun `should emit Error state when loadPosts returns empty list`() = runTest {
        coEvery { interactor.loadPosts() } returns emptyList()

        viewModel = RoutesViewModel(interactor)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is RoutesState.Error)
        assertEquals("Ошибка загрузки ленты", (state as RoutesState.Error).message)
    }

    @Test
    fun `should call like when post is not liked`() = runTest {
        // post1 is not liked
        coEvery { interactor.loadPosts() } returns listOf(post1)
        coEvery { interactor.like(1L) } returns true


        viewModel = RoutesViewModel(interactor)
        advanceUntilIdle()

        viewModel.processIntent(RoutesIntent.RatePost(1L))
        advanceUntilIdle()

        coVerify { interactor.like(1L) }
    }

}
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
        cleanupTestCoroutines()
    }
}
