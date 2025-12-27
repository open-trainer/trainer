# java-trainer

This project is for setting up your training routine.

Entities:
1. UserProfile - user stat data
2. TrainingPlan
3. TrainingPlanVersion

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

