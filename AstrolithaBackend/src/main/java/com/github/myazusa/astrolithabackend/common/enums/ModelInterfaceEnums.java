package com.github.myazusa.astrolithabackend.common.enums;

public enum ModelInterfaceEnums {
    ollama("ollama"),
    python("python");

    private final String modelInterface;
    ModelInterfaceEnums(String modelInterface) {
        this.modelInterface = modelInterface;
    }

    public String getModelInterface() {
        return modelInterface;
    }
    public static ModelInterfaceEnums getFromString(String value) {
        for (ModelInterfaceEnums modelInterfaceEnums : ModelInterfaceEnums.values()) {
            if (modelInterfaceEnums.getModelInterface().equalsIgnoreCase(value)) {
                return modelInterfaceEnums;
            }
        }
        throw new IllegalArgumentException("类型转换失败");
    }
}
