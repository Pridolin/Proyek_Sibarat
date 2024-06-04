const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

async function predictClassification(model, imageBuffer) {
    try {
        const tensor = tf.node.decodeImage(imageBuffer)
            .resizeNearestNeighbor([224, 224])
            .expandDims()
            .toFloat()
            .div(tf.scalar(255.0));  // Normalisasi gambar

        const prediction = model.predict(tensor);
        const scores = prediction.arraySync()[0];

        const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split('');
        const maxScoreIndex = scores.indexOf(Math.max(...scores));
        const label = labels[maxScoreIndex];
        const suggestion = `Predicted character is ${label}.`;

        return { label, suggestion };
    } catch (error) {
        throw new InputError(`${error.message}`);
    }
}

module.exports = predictClassification;