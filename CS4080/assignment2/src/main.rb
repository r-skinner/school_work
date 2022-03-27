j = 0;
=begin
    k = (j + 13) / 27
loop:
    if k > 10 then goto out
    k = k + 1
    i = 3 * k - 1
    goto loop
out: …
=end
k = (j + 13)/27
while k <= 10  do
  k += 1
  i = 3*k-1
end

=begin
if((k == 1) || (k == 2)) j = 2 * k - 1
if((k == 3) || (k == 5)) j = 3 * k + 1
if(k == 4) j = 4 * k - 1
if ((k == 6) || (k == 7) || (k == 8)) j = k - 2
=end

case k
when 1,2
    j = 2*k-1
when 3, 5
    j = 3*k+1
when 4
    j = 4 * k - 1
when 6..8
    j = k - 2
end