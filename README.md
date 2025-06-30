
# 🎥 Snapcast Processamento

![JAVA](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![](https://img.shields.io/badge/Amazon_AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Quarkus](https://img.shields.io/badge/QUARKUS-009CAB?style=for-the-badge&logo=quarkus&logoColor=white)
![Kakfa](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)

Microserviço responsável pelo processamento de vídeos, incluindo download, extração de frames, compactação e upload para o S3.

## ⚡ Principais Funcionalidades

### 🔄 Processamento de Vídeos
- 📥 Download automático de vídeos do S3
- 🎞️ Extração de frames dos vídeos
- 💾 Armazenamento temporário de frames
- 🗜️ Compactação dos frames processados
- 📤 Upload do arquivo compactado para S3

### 🔌 Integrações
- 📦 AWS S3 para armazenamento
- 📬 Sistema de filas para comunicação assíncrona
- 📊 Monitoramento de status do processamento

## 🛠️ Como Executar

```shell
./mvnw quarkus:dev
```

### 🧪 Testes
Execute:
```shell script
./mvnw test
```

## 🔧 Configuração
Necessário configurar as seguintes variáveis de ambiente:

```
KAFKA_URL=
AWS_REGION=
CLIENT_ID=
USER_POOL_ID=
DOMAIN=
BUCKET=
```

## 📜 Documentação API
Swagger UI disponível em: ***/q/swagger-ui***

## 📈 Diagrama de Sequência

[![](https://mermaid.ink/img/pako:eNqNk82OmzAUhV_F8mxJBIYE8KIShPypmqpSpq1UiCo3OAkK2MiYTmaSPEzVRVez7BPkxWpMMk00bTVeIPve79x7bOMdXPCUQgxXgpRrcBclDKgRxFN2fFpkfA46nTcgjCN-z3JOUpBy8PH4lFLezGb2vOVDjQ3i4VYKcvx5_KHSFIwEKWh1QgYaieJAFOSRMpVhkoM7WpRcHL8L1arlIs0N4wEvSrKQ52q8ui431Ngo_lBqWyUR5I-dkU6O41FWqEgbq-qv7R7fC76gVXXh8NLgXoeAtQeRFc9I_o0IsGxCX6xuyVbzv7BIseiaRf9i3ynWvmbZBUtZ-sLvlEm60qf662x1EgefZs1229u53PSkXUzjt2S5IS2wv6WsIiv12YOgzY_bxEwSWavo9GV7-ZBTEIBlluf4xlTDC41KCr6h-Ma27dO8c5-lco1Rub2UjU8yZIYoCF4tm5xkTqi6oVfLps8m-45p_lcGDfWjZynEUtTUgAVVP2OzhLumYALlmhY0gVhNUyI2CUzYQWlKwj5zXpxlgterNcRLkldqVZcpkTTKiLqk4jkq1FlSMeA1kxAj5OoiEO_gFmLLtru25fZcx_cdz7eRZ8AHiH2n6_g913eR1zdRz3IOBnzUbc2u2zMdD_UtzzM91-k7BqRpJrm4bV-vfsSH38_FMNU?type=png)](https://mermaid.live/edit#pako:eNqNk82OmzAUhV_F8mxJBIYE8KIShPypmqpSpq1UiCo3OAkK2MiYTmaSPEzVRVez7BPkxWpMMk00bTVeIPve79x7bOMdXPCUQgxXgpRrcBclDKgRxFN2fFpkfA46nTcgjCN-z3JOUpBy8PH4lFLezGb2vOVDjQ3i4VYKcvx5_KHSFIwEKWh1QgYaieJAFOSRMpVhkoM7WpRcHL8L1arlIs0N4wEvSrKQ52q8ui431Ngo_lBqWyUR5I-dkU6O41FWqEgbq-qv7R7fC76gVXXh8NLgXoeAtQeRFc9I_o0IsGxCX6xuyVbzv7BIseiaRf9i3ynWvmbZBUtZ-sLvlEm60qf662x1EgefZs1229u53PSkXUzjt2S5IS2wv6WsIiv12YOgzY_bxEwSWavo9GV7-ZBTEIBlluf4xlTDC41KCr6h-Ma27dO8c5-lco1Rub2UjU8yZIYoCF4tm5xkTqi6oVfLps8m-45p_lcGDfWjZynEUtTUgAVVP2OzhLumYALlmhY0gVhNUyI2CUzYQWlKwj5zXpxlgterNcRLkldqVZcpkTTKiLqk4jkq1FlSMeA1kxAj5OoiEO_gFmLLtru25fZcx_cdz7eRZ8AHiH2n6_g913eR1zdRz3IOBnzUbc2u2zMdD_UtzzM91-k7BqRpJrm4bV-vfsSH38_FMNU)