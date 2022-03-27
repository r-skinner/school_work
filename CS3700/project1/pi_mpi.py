#!/usr/bin/python3

from mpi4py import MPI
import random

world = MPI.COMM_WORLD
numprocs = world.size
myid = world.rank
procname = MPI.Get_processor_name()

print('Process %d on %s' %(myid, procname))

POINTS = 10000000

random.seed(myid)

totalTime = 0
if myid == 0:
    startwtime = MPI.Wtime()

part_sum = 0
for i in range(POINTS):
    if myid == 0: # master worker
        x = random.uniform(0, 1)
        y = random.uniform(0, 1)
        if (x**2 + y**2)**0.5 <= 1:
            part_sum += 1

    else: # slave worker
        x = random.uniform(0, 1)
        y = random.uniform(0, 1)
        if (x**2 + y**2)**0.5 <= 1:
            part_sum += 1

sum = world.reduce(part_sum, op=MPI.SUM, root=0)

if myid == 0:
    ratio = sum/POINTS/5
    pi = 4 * ratio 

    endwtime = MPI.Wtime()
    runTime = endwtime- startwtime
    print('Execution time (sec) = %f, pi = %f' %(runTime, pi))
    totalTime += runTime

world.barrier()


