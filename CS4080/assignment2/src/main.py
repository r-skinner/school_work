j = 0
'''
    k = (j + 13) / 27
loop:
    if k > 10 then goto out
    k = k + 1
    i = 3 * k - 1
    goto loop
out: …
'''
k = (j + 13)/27
while k <= 10:
    k += 1
    i = 3*k -1

'''
if((k == 1) || (k == 2)) j = 2 * k - 1
if((k == 3) || (k == 5)) j = 3 * k + 1
if(k == 4) j = 4 * k - 1
if ((k == 6) || (k == 7) || (k == 8)) j = k - 2
'''

if k == 1 or k == 2:
    j = 2 * k - 1
elif k == 3 or k == 5:
    j = 3 * k + 1
elif k == 4:
    j = 4 * k - 1
elif k == 6 or k == 7 or k == 8:
    j = k - 2