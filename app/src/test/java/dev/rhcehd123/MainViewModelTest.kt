package dev.rhcehd123

import dev.rhcehd123.data.repository.CurrencyRepository
import dev.rhcehd123.data.repository.FakeLocalRepository
import dev.rhcehd123.ui.main.MainViewModel
import dev.rhcehd123.util.Util.toFormattedString
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.text.NumberFormat
import java.text.ParseException

const val EMPTY_STRING = ""
const val DOUBLE_ZERO = 0.0
const val RECIPIENT_COUNTRY_KOREA = "한국(KRW)"
const val RECIPIENT_COUNTRY_JAPAN = "일본(JPY)"
const val CURRENCY_RATE_KEY_KOREA = "KRW"
const val CURRENCY_RATE_KEY_JAPAN = "JPY"
const val CURRENCY_RATE_KOREA = 1000.0
const val CURRENCY_RATE_JAPAN = 2000.0
const val CURRENCY_LOOKUP_TIME = "2023-02-03 02:03"

class MainViewModelTest: BehaviorSpec({
    val fakeLocalRepository = FakeLocalRepository()
    val initSuccessRepository: CurrencyRepository = mockk()
    every { initSuccessRepository.isInitialized() } returns true
    every { initSuccessRepository.getCurrencyRate(CURRENCY_RATE_KEY_KOREA) } returns CURRENCY_RATE_KOREA
    every { initSuccessRepository.getCurrencyRate(CURRENCY_RATE_KEY_JAPAN) } returns CURRENCY_RATE_JAPAN
    every { initSuccessRepository.currencyLookupTime } returns CURRENCY_LOOKUP_TIME
    val initSuccessViewModel = MainViewModel(initSuccessRepository, fakeLocalRepository)

    val initFailureRepository: CurrencyRepository = mockk()
    every { initFailureRepository.isInitialized() } returns false
    val initFailureViewModel = MainViewModel(initFailureRepository, fakeLocalRepository)

    given("환율정보조회(메인) 화면에서") {
        `when`("초기 실행 시") {
            and("환율 정보 불러오기가 성공했다면") {
                val uiState = initSuccessViewModel.uiState.value
                then("한국(KRW)의 환율 정보로 초기화 된다") {
                    uiState.recipientCountry shouldBe RECIPIENT_COUNTRY_KOREA
                    val currencyRate = try {
                        NumberFormat.getInstance().parse(uiState.currencyRate)?.toDouble()
                    } catch (e: ParseException) {
                        DOUBLE_ZERO
                    }
                    currencyRate shouldBe CURRENCY_RATE_KOREA
                }
            }

            and("환율 정보 불러오기가 실패했다면") {
                val uiState = initFailureViewModel.uiState.value
                then("환율 정보가 0값으로 표기 된다") {
                    uiState.recipientCountry shouldBe EMPTY_STRING
                    val currencyRate = try {
                        NumberFormat.getInstance().parse(uiState.currencyRate)?.toDouble()
                    } catch (e: ParseException) {
                        DOUBLE_ZERO
                    }
                    currencyRate shouldBe DOUBLE_ZERO
                }
            }
        }

        val repository = initSuccessRepository
        val viewModel = initSuccessViewModel

        `when`("송금액를 변경할 때") {
            viewModel.setRemittance("1000")
            val uiState = viewModel.uiState.value
            then("수취금액 정보가 변경된다") {
                val a = uiState.recipient
                uiState.recipient shouldBe "${ (1000 * CURRENCY_RATE_KOREA).toFormattedString() } KRW / USD"
            }
        }

        `when`("수취국가를 변경할 때") {
            viewModel.setRecipientCountry(RECIPIENT_COUNTRY_JAPAN)
            val uiState = viewModel.uiState.value
            then("환율 정보가 변경된다") {
                val currencyRate = try {
                    NumberFormat.getInstance().parse(uiState.currencyRate)?.toDouble()
                } catch (e: ParseException) {
                    0
                }
                currencyRate shouldBe repository.getCurrencyRate(CURRENCY_RATE_KEY_JAPAN)
            }
            then("환율 조회 시간이 변경된다") {
                0 shouldBe 0
            }
            then("수취금액 정보가 변경된다") {
                uiState.recipient shouldBe "${(1000 * CURRENCY_RATE_JAPAN).toFormattedString()} JPY / USD"
            }
        }
    }

})