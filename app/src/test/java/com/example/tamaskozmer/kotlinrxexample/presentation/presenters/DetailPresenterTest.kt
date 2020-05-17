package com.example.tamaskozmer.kotlinrxexample.presentation.presenters

import com.example.tamaskozmer.kotlinrxexample.domain.interactors.GetDetails
import com.example.tamaskozmer.kotlinrxexample.presentation.view.DetailView
import com.example.tamaskozmer.kotlinrxexample.presentation.view.viewmodels.DetailsViewModel
import com.example.tamaskozmer.kotlinrxexample.testutil.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by Tamas_Kozmer on 7/21/2017.
 */
class DetailPresenterTest {

    @Mock
    lateinit var mockGetDetails: GetDetails

    @Mock
    lateinit var mockView: DetailView

    lateinit var detailPresenter: DetailPresenter

    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        detailPresenter = DetailPresenter(mockGetDetails, testSchedulerProvider)
    }

    @Test
    fun testGetDetails_error() {
        // Given
        val error = "Test error"
        val userId = 1L
        val single: Single<DetailsViewModel> = Single.create {
            emitter -> emitter.onError(Exception(error))
        }

        // When
        `when`(mockGetDetails.execute(userId, false)).thenReturn(single)

        detailPresenter.attachView(mockView)
        detailPresenter.getDetails(userId)

        testScheduler.triggerActions()

        // Then
        verify(mockView).showLoading()
        verify(mockView).hideLoading()
        verify(mockView).showError(error)
    }

    @Test
    fun testGetDetails_success() {
        // Given
        val details = DetailsViewModel(emptyList(), emptyList(), emptyList())
        val userId = 1L
        val single: Single<DetailsViewModel> = Single.create {
            emitter -> emitter.onSuccess(details)
        }

        // When
        `when`(mockGetDetails.execute(userId, false)).thenReturn(single)

        detailPresenter.attachView(mockView)
        detailPresenter.getDetails(userId)

        testScheduler.triggerActions()

        // Then
        verify(mockView).showLoading()
        verify(mockView).hideLoading()
        verify(mockView).showDetails(details)
    }
}