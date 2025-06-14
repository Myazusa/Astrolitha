export interface ToolFunction {
    toolUUID: string;
    name:string;
    enabled:boolean;
    functionName: string;
    toolDescription: string;
    remoteApi: string;
    requestMethod: string;
}