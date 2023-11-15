package com.smuraha.currency_rates.service.util;

import com.smuraha.currency_rates.firebase.entity.CurrencyRate;
import com.smuraha.currency_rates.service.enums.CallBackParams;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smuraha.currency_rates.service.enums.CallBackParams.P;

@Service
@RequiredArgsConstructor
public class TelegramUIImpl implements TelegramUI {

    private final JsonMapper jsonMapper;

    @Override
    public SendMessage getMessageWithButtons(List<List<InlineKeyboardButton>> buttons, String text,Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    @Override
    public String getFormedRate(CurrencyRate rate) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>").append(rate.getCurrency()).append("</b> \uD83D\uDCB0").append("\n");
        if(rate.getRateOfficial()!=null){
            builder.append("✅ Официальный курс ").append(rate.getCurrency()).append(" : ").append(rate.getRateOfficial()).append("\n");
        }else {
            builder.append("❌ Сдать ").append(rate.getCurrency()).append(" : ").append(rate.getRateBuy()).append("\n")
                    .append("✅ Купить ").append(rate.getCurrency()).append(" : ").append(rate.getRateSell()).append("\n");
        }
        builder.append("\uD83D\uDCB1").append(" Цена за ").append(rate.getScale()).append(" ").append(rate.getCurrency()).append("\n")
        .append("\uD83D\uDD57").append(" ").append(rate.getLastUpdate()).append("\n")
                .append("\n");
        return builder.toString();
    }
//
//    @Override
//    public List<List<InlineKeyboardButton>> getCurrencyButtons(List<Currencies> currencies, CallBackKeys cbs) throws JsonProcessingException {
//        List<InlineKeyboardButton> currenciesKeyBoard = new ArrayList<>();
//        for (Currencies currency : currencies) {
//            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//            inlineKeyboardButton.setText(currency.name());
//            HashMap<CallBackParams, String> params = new HashMap<>();
//            params.put(C, currency.name());
//            inlineKeyboardButton.setCallbackData(jsonMapper.writeCustomCallBackAsString(
//                    new CustomCallBack(cbs, params)
//            ));
//            currenciesKeyBoard.add(inlineKeyboardButton);
//        }
//        List<List<InlineKeyboardButton>> currencyButtons = new ArrayList<>();
//        currencyButtons.add(currenciesKeyBoard);
//        return currencyButtons;
//    }
//
    @Override
    public List<InlineKeyboardButton> getCustomPager(CustomCallBack customCallBack, int page, int totalPages) {
        List<InlineKeyboardButton> pager = new ArrayList<>();

        Map<CallBackParams, String> params = customCallBack.getPrms();
        Integer order = customCallBack.getOrder();
        String id = customCallBack.getId();

        if (page > 0) {
            InlineKeyboardButton prev = new InlineKeyboardButton();
            prev.setText("←");
            params.put(P, (page - 1) + "");
            prev.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                    new CustomCallBack(id, order,params)
            ));
            pager.add(prev);
        }
        InlineKeyboardButton cur = new InlineKeyboardButton();
        cur.setText("" + (page + 1) + "/" + totalPages);
        cur.setCallbackData("IGNORE");
        pager.add(cur);
        if (page < totalPages - 1) {
            InlineKeyboardButton next = new InlineKeyboardButton();
            next.setText("→");
            params.put(P, (page + 1) + "");
            next.setCallbackData(jsonMapper.writeCustomCallBackAsString(
                    new CustomCallBack(id, order,params)
            ));
            pager.add(next);
        }
        return pager;
    }

    private byte[] getImageOfChartInBytes(Map<LocalDate, BigDecimal> data) throws IOException {
        int width = 1920;
        int height = 1080;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Улучшаем качество отрисовки
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        int padding = 70;
        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;

        // Определяем минимальное и максимальное значение на оси Y

        List<BigDecimal> listOfValues = data.values().stream().toList();
        BigDecimal maxValue = listOfValues.stream().max(BigDecimal::compareTo).get();
        BigDecimal minValue = listOfValues.stream().min(BigDecimal::compareTo).get();
        List<BigDecimal> setOfValues = new ArrayList<>();
        for (BigDecimal i = minValue; i.compareTo(maxValue) <= 0; i = i.add(new BigDecimal("0.05"))) {
            setOfValues.add(i);
        }

        // Определяем количество точек на графике
        int numPoints = data.size();

        // Определяем шаг по оси X
        double xStep = (double) graphWidth / (numPoints-2);

        // Определяем шаг по оси Y
        double yStep = (double) graphHeight / (maxValue.subtract(minValue)).doubleValue();

        // Рисуем оси
        g2d.drawLine(padding, height - padding, padding, padding - 40);
        g2d.drawLine(padding, height - padding, width - padding, height - padding);

        // пдписываем ось y
        for (BigDecimal value : setOfValues) {
            int y = height - 30 - padding - ((value.subtract(minValue)).multiply(new BigDecimal(yStep))).intValue();
            g2d.drawLine(padding - 5, y, padding + 5, y);
            g2d.drawString(value.toString(), padding - 30, y);
        }

        // Рисуем точки и соединяем их линиями
        int x = padding;
        int y = 0;
        //int ySellPrev = 0;
        for (Map.Entry<LocalDate, BigDecimal> entry : data.entrySet()) {
            String key = entry.getKey().toString().substring(5);
            double buy = entry.getValue().doubleValue();
            //double sell = entry.getValue().get(1).doubleValue();

            // Рисуем точку
            int yBuy = height - 30 - padding - (int) ((buy - minValue.doubleValue()) * yStep);
            //int ySell = height - 30 - padding - (int) ((sell - minValue.doubleValue()) * yStep);
            g2d.fillOval(x - 2, yBuy - 2, 4, 4);
            //g2d.fillOval(x - 2, ySell - 2, 4, 4);
            if (y != 0) {
                g2d.setColor(Color.BLUE);
                g2d.drawLine((int) (x - xStep), y, x, yBuy);
            }
            g2d.setColor(Color.BLACK);
            y = yBuy;

            // Переходим к следующей точке по оси X
            x += xStep;
        }

        g2d.dispose();

        // Convert the image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    @Override
    public void drawChart(Map<LocalDate, BigDecimal> data, String chatId) throws IOException {
        byte[] imageBytes = getImageOfChartInBytes(data);
        // Send the image to the Telegram bot
        sendImageToTelegramBot(imageBytes, chatId);
    }

    private void sendImageToTelegramBot(byte[] imageBytes, String chatId) throws IOException {
        // Telegram bot API endpoint
        String botToken = System.getenv("BOT_TOKEN");
        String botApiUrl = "https://api.telegram.org/bot" + botToken + "/sendDocument";

        // Create an HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create an HTTP POST request with the image data
        HttpPost httpPost = new HttpPost(botApiUrl);
        ByteArrayBody imageBody = new ByteArrayBody(imageBytes, ContentType.IMAGE_PNG, "image.png");

        // Build the multipart/form-data request entity
        HttpEntity requestEntity = MultipartEntityBuilder.create()
                .addPart("chat_id", new StringBody(chatId, ContentType.TEXT_PLAIN))
                .addPart("document", imageBody)
                .build();

        // Set the request entity
        httpPost.setEntity(requestEntity);

        // Execute the request
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // Close the response and the HTTP client
        response.close();
        httpClient.close();
    }
}
