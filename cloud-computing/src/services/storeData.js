const { Firestore } = require('@google-cloud/firestore');

const db = new Firestore();

async function storeData(sessionId, data) {
    const predictCollection = db.collection('predictions');
    return predictCollection.doc(sessionId).set(data);
}

module.exports = storeData;
