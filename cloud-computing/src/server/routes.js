const {
    postPredictHandler,
    getPredictHandler,
} = require("../server/handler");

const routes = [
    {
        path: "/predict",
        method: "POST",
        handler: postPredictHandler,
        options: {
            payload: {
                /*Mengizinkan data berupa gambar*/
                allow: "multipart/form-data",
                maxBytes: 1000000,
                multipart: true,
            },
        },
    },
    {
        path: "/predict/{sessionId}",
        method: "GET",
        handler: getPredictHandler,
    },
];

module.exports = routes;
