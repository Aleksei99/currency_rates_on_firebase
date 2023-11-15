package com.smuraha.currency_rates.service.processor.commandScripts;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.firebase.entity.Subscription;
import com.smuraha.currency_rates.firebase.entity.User;
import com.smuraha.currency_rates.firebase.entity.dto.CurrencyRatesWithTotalPageSize;
import com.smuraha.currency_rates.service.BankService;
import com.smuraha.currency_rates.service.UserService;
import com.smuraha.currency_rates.service.bankApi.IBank;
import com.smuraha.currency_rates.service.bankApi.scheduler.subscription.SchedulerManager;
import com.smuraha.currency_rates.service.enums.CallBackParams;
import com.smuraha.currency_rates.service.enums.Commands;
import com.smuraha.currency_rates.service.util.CustomCallBack;
import com.smuraha.currency_rates.service.util.JsonMapper;
import com.smuraha.currency_rates.service.util.TelegramUI;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

import static com.smuraha.currency_rates.service.enums.CallBackParams.*;

@Service
@RequiredArgsConstructor
public class CommandScriptServiceUnsubscribe implements CommandScriptService {

    private final List<IBank> banks;
    private final JsonMapper jsonMapper;
    private final TelegramUI telegramUI;
    private final UserService userService;
    private final BankService bankService;
    private final SchedulerManager schedulerManager;

    private final static int PAGE_SIZE = 5;

    @Override
    public SendMessage launchScript(Update update) {
        String text = "Выберите банк из списка: ";
        Long chatId = update.getMessage().getChatId();

        List<List<InlineKeyboardButton>> banks_KB = new ArrayList<>();

        Map<CallBackParams, String> params = new HashMap<>();
        List<IBank> subscribedBanksOfUser = getSubscribedBanksOfUserByUserId(chatId);
        for (IBank bank : subscribedBanksOfUser) {
            List<InlineKeyboardButton> row_banks_KB = new ArrayList<>();
            InlineKeyboardButton cell_bank_KB = new InlineKeyboardButton();
            cell_bank_KB.setText(bank.getBankName());
            params.put(B, bank.getBankId());
            cell_bank_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                    new CustomCallBack(getCommandId(), 1, params)
            ));
            row_banks_KB.add(cell_bank_KB);
            banks_KB.add(row_banks_KB);
        }
        return telegramUI.getMessageWithButtons(banks_KB, text, chatId);
    }

    @SneakyThrows
    @Override
    public SendMessage processCallback(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        CustomCallBack callBack = jsonMapper.readCustomCallBack(update.getCallbackQuery().getData());
        Map<CallBackParams, String> params = callBack.getPrms();
        switch (callBack.getOrder()) {
            case 0 -> {
                update.setMessage(update.getCallbackQuery().getMessage());
                return launchScript(update);
            }
            case 1 -> {

                String text = "Выберите валюту от которой хотите отписаться:";
                List<List<InlineKeyboardButton>> cur_KB = new ArrayList<>();
                int page = params.containsKey(P) ? Integer.parseInt(params.get(P)) : 0;
                String bankId = params.get(B);

                List<CurrencyRate> rates = bankService.getCurrencyRatesByBankId(bankId);
                User user = userService.findUserById(chatId);
                List<String> subscribedOnCurs = user.getSubscriptions()
                        .stream()
                        .filter(subscription -> subscription.getBankId().equals(bankId))
                        .map(Subscription::getCurrency)
                        .collect(Collectors.toList());
                rates = rates.stream().filter(rate -> subscribedOnCurs.contains(rate.getCurrency())).collect(Collectors.toList());
                CurrencyRatesWithTotalPageSize ratesWithSize = bankService.getCurrencyRatesWithTotalPageSizeByBankId(page, PAGE_SIZE, rates).get();

                for (CurrencyRate rate : ratesWithSize.getRates()) {
                    List<InlineKeyboardButton> row_cur_KB = new ArrayList<>();
                    InlineKeyboardButton cell_cur_KB = new InlineKeyboardButton();
                    cell_cur_KB.setText(rate.getCurrency());
                    params.put(C, rate.getCurrency());
                    cell_cur_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                            new CustomCallBack(getCommandId(), 2, params)
                    ));
                    row_cur_KB.add(cell_cur_KB);
                    cur_KB.add(row_cur_KB);
                }

                List<InlineKeyboardButton> pager = telegramUI.getCustomPager(callBack, page, ratesWithSize.getTotalPageSize());
                InlineKeyboardButton back_KB = new InlineKeyboardButton();
                back_KB.setText("Назад");
                back_KB.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                        new CustomCallBack(getCommandId(), 0, params)
                ));
                pager.add(0, back_KB);
                cur_KB.add(pager);
                return telegramUI.getMessageWithButtons(cur_KB, text, chatId);
            }
            case 2 -> {
                User user = userService.findUserById(chatId);
                String bankId = params.get(B);
                String currency = params.get(C);
                Subscription subscription = user.deactivateSubscription(bankId, currency);
                userService.updateUser(user);
                schedulerManager.stopSubscriptionJob(subscription);
                String textMessage = String.format("""
                        Ваша подписка на валюту %s банка %s
                        успешно отменена.
                        """, currency, IBank.getBankNameByBankId(banks, bankId));
                SendMessage sendMessage = new SendMessage();
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setChatId(chatId);
                sendMessage.setText(textMessage);
                return sendMessage;
            }
        }
        return null;
    }

    @Override
    public String getCommandId() {
        return "6";
    }

    @Override
    public Commands getScriptCommand() {
        return Commands.UNSUBSCRIBE;
    }

    private List<IBank> getSubscribedBanksOfUserByUserId(Long id) {
        User user = userService.findUserById(id);
        List<Subscription> subscriptions = user.getSubscriptions();
        Set<String> bankIds = subscriptions.stream().map(Subscription::getBankId).collect(Collectors.toSet());
        return banks.stream().filter(iBank -> bankIds.contains(iBank.getBankId())).collect(Collectors.toList());
    }
}
