FROM node:18
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
ENV PORT 4136 
ENV MODEL_URL='https://storage.googleapis.com/sibarat-c241-bucket/new-model-ari/model.json'
CMD ["npm", "run", "start"]