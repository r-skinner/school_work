#!/bin/bash
#SBATCH --job-name=RUN_LOG
#SBATCH --output=RUN_LOG_%j.txt
#SBATCH --partition=gpu
#SBATCH --gres=gpu:gp100gl:2
#SBATCH --time=00:02:00

. /etc/profile.d/modules.sh

module load cuda/8 

./matrix
