const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

async function predictClassification(model, imageBuffer) {
    try {
        console.log("Starting image processing");

        // Decode image buffer, resize to [28, 28], convert to grayscale, and normalize
        const tensor = tf.node.decodeImage(imageBuffer, 1)  // Decode image with 1 channel (grayscale)
            .resizeNearestNeighbor([28, 28])  // Resize to [28, 28]
            .expandDims()  // Add batch dimension
            .toFloat()  // Convert to float
            .div(tf.scalar(255.0));  // Normalize to range [0, 1]

        console.log("Image decoded:", tensor.shape);

        // Ensure tensor shape is [1, 28, 28, 1]
        const inputTensor = tensor.reshape([1, 28, 28, 1]);
        console.log("Image resized and normalized:", inputTensor.shape);

        console.log("Starting model prediction");
        const prediction = model.predict(inputTensor);
        const scores = prediction.arraySync()[0];
        console.log("Prediction scores:", scores);

        const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split('');
        const maxScoreIndex = scores.indexOf(Math.max(...scores));
        const label = labels[maxScoreIndex];
        const suggestion = `Predicted character is ${label}.`;

        return { label, suggestion };
    } catch (error) {
        console.error("Prediction error:", error);
        throw new InputError(`Prediction failed: ${error.message}`);
    }
}

module.exports = predictClassification;