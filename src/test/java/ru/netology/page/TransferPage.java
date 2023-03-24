package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public void transferMoney(int transferAmount, DataHelper.CardInfo cardInfo) {
        $("[data-test-id='amount'] .input__control").setValue(String.valueOf(transferAmount));
        $("[data-test-id='from'] .input__control").setValue(cardInfo.getCardNumber());
        $("[data-test-id='action-transfer']").click();
    }

    public void transferError() {
        $("Недостаточно средств на счете для выполнения данной операции").shouldBe(Condition.visible);
    }
}