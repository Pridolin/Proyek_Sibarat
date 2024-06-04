const predictClassification = require("../services/inferenceService");
const crypto = require("crypto");
const storeData = require("../services/storeData");
const getData = require("../services/getData");

async function postPredictHandler(request, h) {
    const { image } = request.payload;
    const { model } = request.server.app;
    const { sessionId } = request.headers;

    if (!image) {
        throw new InputError('Tidak ada file yang tersedia');
    }

    if (!sessionId) {
        throw new InputError('Tidak ada ID sesi yang diberikan');
    }

    const { label, suggestion } = await predictClassification(model, image);

    const id = crypto.randomUUID();
    const createdAt = new Date().toISOString();

    const data = {
        id: id,
        result: label,
        suggestion: suggestion,
        createdAt: createdAt,
    };

    await storeData(sessionId, data);

    const response = h.response({
        status: "success",
        message: "Model is predicted successfully",
        data,
    });
    response.code(201);
    return response;
}

async function getPredictHandler(request, h) {
    const { sessionId } = request.params;

    if (!sessionId) {
        throw new InputError('Tidak ada ID sesi yang diberikan');
    }

    const data = await getData(sessionId);

    if (!data) {
        const response = h.response({
            status: "fail",
            message: "Tidak ada data yang ditemukan untuk sesi ini",
        });
        response.code(404);
        return response;
    }

    const response = h.response({
        status: "success",
        data,
    });
    response.code(200);
    return response;
}

module.exports = { postPredictHandler, getPredictHandler };