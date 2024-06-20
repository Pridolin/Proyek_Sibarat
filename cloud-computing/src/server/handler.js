const InputError = require('../exceptions/InputError');
const predictClassification = require("../services/inferenceService");
const crypto = require("crypto");
const storeData = require("../services/storeData");
const getData = require("../services/getData");

async function postPredictHandler(request, h) {
    console.log("postPredictHandler called");

    const { image } = request.payload;
    const { model } = request.server.app;

    console.log("Headers:", request.headers); // Log semua header yang diterima

    if (!image) {
        console.error("Tidak ada file yang tersedia");
        throw new InputError('Tidak ada file yang tersedia');
    } else {
        console.log("Image received:", image);
    }

    try {
        console.log("Starting prediction");
        const { label, suggestion } = await predictClassification(model, image);
        console.log("Prediction successful");

        const id = crypto.randomUUID();
        const createdAt = new Date().toISOString();

        const data = {
            id: id,
            result: label,
            suggestion: suggestion,
            createdAt: createdAt,
        };

        console.log("Storing data:", data);
        await storeData(id, data);

        const response = h.response({
            status: "success",
            message: "Model is predicted successfully",
            data,
        });
        response.code(201);
        return response;
    } catch (error) {
        console.error("Error in postPredictHandler:", error);
        throw new InputError('Terjadi kesalahan dalam melakukan prediksi');
    }
}

async function getPredictHandler(request, h) {

    const data = await getData("\(default\)");

    const response = h.response({
        status: "success",
        data,
    });
    response.code(200)
    return response;
}

module.exports = { postPredictHandler, getPredictHandler };