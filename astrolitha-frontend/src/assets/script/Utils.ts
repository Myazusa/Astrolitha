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

export function noSpaces(obj: any) :boolean{
    return Object.values(obj).every(
        val => typeof val === 'string' && !/\s/.test(val)
    )
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

export function noSpecialChars(obj: any) :boolean{

    return Object.values(obj).every(
        val => typeof val === 'string' && isValidJavaMethodName(val)
    )
}
export function noEmpty(obj: any) :boolean{
    return Object.values(obj).every(
        val => typeof val === 'string' && val === '')
}