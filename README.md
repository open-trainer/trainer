# java-trainer

This projects is for setting up your training routine.

Architecture diagram (Excalidraw): [docs/trainer.excalidraw](docs/trainer.excalidraw)

# Stack:
* JAVA
* Postgresql
* AWS

## AWS deployment flow
```
GitHub
↓
GitHub Actions (CI/CD)
↓
Docker image
↓
Amazon ECR (image registry)
↓
EC2 (Docker runtime)
```

