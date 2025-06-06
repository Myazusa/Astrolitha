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