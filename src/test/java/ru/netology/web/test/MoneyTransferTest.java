package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV1;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;

class MoneyTransferTest {

    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        //проверка валидности
        dashboardPage = verificationPage.validVerify(verificationCode);
        //получаем карты
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        //получаем баланс карты
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    }
    @Test
    void shouldTransferFirstSecond()
    {
        int amount = DataHelper.generateValidAmount(firstCardBalance);
        int expectedBalanceCardFirst = firstCardBalance - amount;
        int expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount),firstCardInfo);
        dashboardPage.reloadDashboarPage();
        assertAll(
                ()-> dashboardPage.checkCardBalance(firstCardInfo,expectedBalanceCardFirst),
                ()-> dashboardPage.checkCardBalance(secondCardInfo,expectedBalanceSecondCard)
        );
    }

    @Test
    void shouldTransferError()
    {
        int amount = DataHelper.generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount),secondCardInfo);
        assertAll(
                () -> transferPage.findErrorMesage("Выполнена попытка перевода суммы, превышающей остаток на карте списания"),
                () -> dashboardPage.reloadDashboarPage(),
                () -> dashboardPage.checkCardBalance(firstCardInfo,firstCardBalance),
                () -> dashboardPage.checkCardBalance(secondCardInfo,secondCardBalance)
                );
    }

}

