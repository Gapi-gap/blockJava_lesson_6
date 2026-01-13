package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        transferHead.shouldBe(visible);
    }

    public DashboardPage makeValidTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountTransfer, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountTransfer);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void findErrorMesage(String expectedText) {
        errorMessage.should(visible, Duration.ofSeconds(15)).should(Condition.text(expectedText));
    }
}
