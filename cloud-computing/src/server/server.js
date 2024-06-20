require("dotenv").config();

const Hapi = require("@hapi/hapi");
const routes = require("../server/routes");
const loadModel = require("../services/loadModel");
const InputError = require("../exceptions/InputError");

(async () => {
    const server = Hapi.server({
        port: 4136,
        host: "0.0.0.0",
        routes: {
            cors: {
                origin: ["*"],
            },
        },
    });

    const model = await loadModel();
    server.app.model = model;

    // Middleware untuk log semua header
    server.ext('onRequest', (request, h) => {
        console.log("Received headers:", request.headers);
        return h.continue;
    });

    server.route(routes);

    server.ext("onPreResponse", function (request, h) {
        const response = request.response;

        if (response instanceof InputError) {
            const newResponse = h.response({
                status: "fail",
                message: "Terjadi kesalahan dalam melakukan prediksi",
            });
            newResponse.code(400);
            return newResponse;
        }

        if (response.isBoom) {
            const newResponse = h.response({
                status: "fail",
                message: response.message,
            });
            newResponse.code(413);
            return newResponse;
        }

        return h.continue;
    });

    await server.start();
    console.log(`Server start at: ${server.info.uri}`);
})();
