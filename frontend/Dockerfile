# Étape 1 : Build de l'app React avec Node
FROM node:18 AS build

# Dossier de travail
WORKDIR /app

# Copier les dépendances et installer
COPY package*.json ./
RUN npm install

# Copier le code source et builder
COPY . .
RUN npm run build

# Étape 2 : Serveur NGINX pour prod
FROM nginx:alpine

# Supprimer la config NGINX par défaut
RUN rm -rf /etc/nginx/conf.d/default.conf

# Copier la config custom
COPY nginx.conf /etc/nginx/conf.d

# Copier le build React dans le dossier web de NGINX
COPY --from=build /app/build /usr/share/nginx/html

# Exposer le port
EXPOSE 80

# Lancer NGINX
CMD ["nginx", "-g", "daemon off;"]
