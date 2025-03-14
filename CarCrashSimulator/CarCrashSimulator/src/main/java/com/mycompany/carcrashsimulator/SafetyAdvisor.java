package com.mycompany.carcrashsimulator;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class SafetyAdvisor {
    private Classifier classifier;
    private Instances structure;

    public SafetyAdvisor(String modelPath, Instances structure) {
        try {
            classifier = (Classifier) SerializationHelper.read(modelPath);
            this.structure = structure;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức nhận các thông số của 2 xe và trả về lời khuyên kèm giải thích
    public String getAdvice(double mass1, double speed1, double safetyFactor1, double hardness1,
                            double mass2, double speed2, double safetyFactor2, double hardness2) {
        try {
            Instance instance = new DenseInstance(structure.numAttributes());
            instance.setDataset(structure);
            instance.setValue(0, mass1);
            instance.setValue(1, speed1);
            instance.setValue(2, safetyFactor1);
            instance.setValue(3, hardness1);
            instance.setValue(4, mass2);
            instance.setValue(5, speed2);
            instance.setValue(6, safetyFactor2);
            instance.setValue(7, hardness2);
            double prediction = classifier.classifyInstance(instance);
            if (prediction < 0.5) {
                return "Khuyến nghị: Xe 1 có khả năng chịu va đập kém hơn xe 2 do khối lượng và độ cứng thấp hơn. Cần cải thiện cấu trúc bảo hộ.";
            } else {
                return "Khuyến nghị: Cấu trúc của xe 1 cho kết quả tốt hơn so với xe 2. Tuy nhiên, hãy cân nhắc nâng cấp hệ thống an toàn.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Không có lời khuyên.";
    }
    
    // Phương thức tải cấu trúc dữ liệu (Instances) từ file ARFF (chỉ để định dạng thuộc tính)
    public static Instances loadStructure(String arffFilePath) {
        try {
            DataSource source = new DataSource(arffFilePath);
            Instances data = source.getDataSet();
            data.clear();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
