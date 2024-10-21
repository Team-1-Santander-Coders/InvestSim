package com.team1.investsim.utils;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class DataAssetPredictionUtil {
    private static final Random random = new Random();
    public static HistoricalDataEntity predictAssetHistoricalData(AssetEntity asset, LocalDateTime futureDate) {
        String pmmlName = String.format("model_%S.pmml", asset.getTicker());
        ModelEvaluator<?> modelEvaluator;
        try {
            modelEvaluator = loadStockPricePMML(pmmlName);
            HistoricalDataEntity lastData = asset.getHistoricalData().getLast();
            return predictFuturePrices(lastData, futureDate, modelEvaluator);
        } catch (Exception e) {
            return null;
        }
    }

    private static ModelEvaluator<?> loadStockPricePMML(String pmmlName) throws Exception {
        ClassPathResource resource = new ClassPathResource("models/" + pmmlName);
        try (InputStream inputStream = resource.getInputStream()) {
            org.dmg.pmml.PMML pmml = org.jpmml.model.PMMLUtil.unmarshal(inputStream);
            ModelEvaluatorBuilder modelEvaluatorBuilder = new ModelEvaluatorBuilder(pmml);
            return modelEvaluatorBuilder.build();
        }
    }

    private static HistoricalDataEntity predictFuturePrices(HistoricalDataEntity lastData, LocalDateTime futureDate, ModelEvaluator<?> modelEvaluator) {
        Map<String, Double> arguments = new LinkedHashMap<>();

        arguments.put("Open", lastData.getOpenPrice().doubleValue());
        arguments.put("High", lastData.getHighPrice().doubleValue());
        arguments.put("Low", lastData.getLowPrice().doubleValue());
        arguments.put("Volume", (double) lastData.getVolume());
        arguments.put("Year", (double) futureDate.getYear());
        arguments.put("Month", (double) futureDate.getMonthValue());
        arguments.put("Day", (double) futureDate.getDayOfMonth());
        arguments.put("Weekday", (double) futureDate.getDayOfWeek().getValue() - 1);

        Map<String, ?> results = modelEvaluator.evaluate(arguments);

        double predictedClose = Double.parseDouble(results.get("Close").toString()
                .replaceAll("\\{result=|}", ""));;

        HistoricalDataEntity predictedData = new HistoricalDataEntity();
        predictedData.setDate(futureDate);
        predictedData.setOpenPrice(BigDecimal.valueOf(randomizePrice(lastData.getOpenPrice().doubleValue())));
        predictedData.setHighPrice(BigDecimal.valueOf(randomizePrice(lastData.getHighPrice().doubleValue())));
        predictedData.setLowPrice(BigDecimal.valueOf(randomizePrice(lastData.getLowPrice().doubleValue())));
        predictedData.setClosePrice(BigDecimal.valueOf(predictedClose));
        predictedData.setVolume(randomizeVolume(lastData.getVolume()));
        predictedData.setAsset(lastData.getAsset());

        return predictedData;
    }

    private static double randomizePrice(double originalPrice) {
        double stdDev = originalPrice * 0.1;
        double randomValue = originalPrice + stdDev * random.nextGaussian();
        return Math.abs(randomValue);
    }

    private static long randomizeVolume(long originalVolume) {
        double stdDev = originalVolume * 0.30;
        double limitedGaussian = Math.max(Math.min(random.nextGaussian(), 2), -2);
        double randomValue = originalVolume + stdDev * limitedGaussian;
        return Math.round(Math.abs(randomValue));
    }
}
