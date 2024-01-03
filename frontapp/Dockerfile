# 첫 번째 스테이지: 빌드 스테이지
FROM node:20.10.0-alpine

# 작업 디렉토리 설정
WORKDIR /app

COPY . .

RUN npm install
RUN npm build

EXPOSE 3000

CMD [ "npm run", "start" ]