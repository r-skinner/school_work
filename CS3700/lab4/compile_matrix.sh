#!/bin/bash
#SBATCH --job-name=COMPILE_LOG
#SBATCH --output=COMPILE_LOG_%j.txt
#SBATCH --partition=gpu
#SBATCH --gres=gpu:gp100gl:2
#SBATCH --time=00:02:00

. /etc/profile.d/modules.sh

module load cuda/8 

nvcc -arch=compute_60 -code=sm_60 -o matrix matrix.cu

