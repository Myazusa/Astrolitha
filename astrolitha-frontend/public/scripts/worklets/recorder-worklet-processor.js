class RecorderWorkletProcessor extends AudioWorkletProcessor {
    constructor() {
        super()
    }

    process(inputs) {
        const input = inputs[0]
        if (input.length > 0) {
            const channelData = input[0] // 单声道
            this.port.postMessage(channelData)
        }
        return true
    }
}

registerProcessor('recorder-worklet-processor', RecorderWorkletProcessor)