const { Firestore } = require('@google-cloud/firestore');

async function getData(sessionId) {
    const db = new Firestore();
    const predictRef = db.collection('predictions').doc(sessionId);
    const doc = await predictRef.get();

    if (!doc.exists) {
        return null;
    }

    return doc.data();
}

module.exports = getData;
