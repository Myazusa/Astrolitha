export interface Message {
    role: 'user' | 'assistant',
    name: string,
    content: string
}