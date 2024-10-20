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

public class DataAssetPredictionUtil {
    public static HistoricalDataEntity predictAssetHistoricalData(AssetEntity asset, LocalDateTime futureDate) throws Exception {
        String pmmlName = String.format("model_%S.pmml", asset.getTicker());
        ModelEvaluator<?> modelEvaluator = loadStockPricePMML(pmmlName);
        HistoricalDataEntity lastData = asset.getHistoricalData().getLast();
        HistoricalDataEntity predictedData = predictFuturePrices(lastData, futureDate, modelEvaluator);
        predictedData.setAsset(asset);
        return predictedData;
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
        arguments.put("Close", lastData.getClosePrice().doubleValue());
        arguments.put("Volume", (double) lastData.getVolume());

        String results = modelEvaluator.evaluate(arguments).get("Close")
                .toString()
                .replaceAll("\\{result=|\\}", "");

        HistoricalDataEntity predictedData = new HistoricalDataEntity();
        predictedData.setDate(futureDate);
        predictedData.setOpenPrice(lastData.getOpenPrice());
        predictedData.setHighPrice(lastData.getHighPrice());
        predictedData.setLowPrice(lastData.getLowPrice());
        predictedData.setClosePrice(BigDecimal.valueOf(Double.parseDouble(results)));
        predictedData.setVolume(lastData.getVolume());

        return predictedData;
    }
}
