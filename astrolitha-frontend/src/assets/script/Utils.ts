import {ToolFunction} from "@/interface/ToolFunction";

export function extractAndRemoveEPlaceholders(input: string): { placeholders: string[], cleaned: string }{
    const regex = /\{#e(.*?)\}/g;

    const placeholders: string[] = [];
    let match;
    while ((match = regex.exec(input)) !== null) {
        placeholders.push(match[1]);
    }

    const cleaned = input.replace(regex, '');

    return { placeholders, cleaned };
}

export function removeEnglishCharacters(input: string) :string{
    return input.replace(/[a-zA-Z]/g, '');
}

export function noSpaces(obj: ToolFunction) :boolean{
    return !/\s/.test(obj.functionName) && !/\s/.test(obj.remoteApi);
}


const javaMethodNamePattern = /^[a-zA-Z_$][a-zA-Z0-9_$]*$/;
const javaKeywords = new Set([
    'abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch', 'char',
    'class', 'const', 'continue', 'default', 'do', 'double', 'else', 'enum',
    'extends', 'final', 'finally', 'float', 'for', 'goto', 'if', 'implements',
    'import', 'instanceof', 'int', 'interface', 'long', 'native', 'new',
    'package', 'private', 'protected', 'public', 'return', 'short', 'static',
    'strictfp', 'super', 'switch', 'synchronized', 'this', 'throw', 'throws',
    'transient', 'try', 'void', 'volatile', 'while'
]);
function isValidJavaMethodName(name: string): boolean {
    return javaMethodNamePattern.test(name) && !javaKeywords.has(name);
}

export function noSpecialChars(obj: ToolFunction) :boolean{
    return isValidJavaMethodName(obj.functionName) && isValidJavaMethodName(obj.requestMethod)
}
export function noEmpty(obj: ToolFunction) :boolean{
    return obj.name !== '' && obj.functionName !== '' && obj.remoteApi !== '' && obj.toolDescription !== '';
}

type Offset = { x: number; y: number }

export function calculateAdaptedOffset(
    currentScreen: { width: number; height: number },
    referenceScreen: { width: number; height: number },
    referenceOffset: Offset
): Offset {
    // 原始左边距
    let leftMargin = -400

    // 增宽
    let incWidth = currentScreen.width - referenceScreen.width

    // 公式可以简化
    let x = (leftMargin + incWidth + 480) / 2.0
    let y = referenceOffset.y
    return {
        x,y
    }
}