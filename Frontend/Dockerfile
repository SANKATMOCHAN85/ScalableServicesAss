FROM nginx:alpine

EXPOSE 80

ENV port=32000

RUN mkdir /ui/

COPY dist /ui/dist

RUN chmod -R 777 /ui
 
COPY nginx.conf /etc/nginx/nginx.conf

ENTRYPOINT ["nginx", "-g", "daemon off;"]