const tf = require('@tensorflow/tfjs-node');

async function loadModel() {
    try {
        const model = await tf.loadLayersModel(process.env.MODEL_URL);
        console.log("Model loaded successfully");
        return model;
    } catch (error) {
        console.error("Model loading error:", error);
        throw new Error('Model loading failed');
    }
}

module.exports = loadModel;