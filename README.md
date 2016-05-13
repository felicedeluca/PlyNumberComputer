# PLY

Algorithm to compute the ply of a given drawing using based on the _line-sweep_ technique.

This algorithm, in order to be robust, takes several minutes to calculate the ply of a drawing.\
Due to _Double_ and _Float_ limits, is it uses [Apfloat](http://www.apfloat.org)  to reduce approximation and caclulation issues. _Apfloat_ is a High Performance Arbitrary Precision Arithmetic Package for C++ and Java.\

### Version
1.0


### Installation

This project uses Maven. You need to upgrade the progect using Maven.

### _

#### Robustness

In order to be robust, all the numbers are expressed using the _Apfloat_ framework configured with _1.000_ decimal digits: Using so many digits reduces the probability of error due to approximations of irrational numbers, increasing the global *Robustness*.

Also, _Duplicated events_ are added to the sweep line ensuring that the intersections between the _circles_ and the _Sweep-Line_ are non-degenerate intervals: between two consecutive events’x-coordinate the status of the drawing (and so the ply) doesn’t change. On the event point the circles overlap in a degenerate range, while in the middle of the two events the overlap of the circles is higher than the other points.

#### Known Issues

_Sqrt_ operation may introduce approximation errors when the events or the intersections are computed. For example the intersection points between two cirlces _c0_ and _c1_ may be different if are computed passing (_c0_, _c1_) and (_c1_, _c0_).\
Since there are the _Duplicated events_ this issue is not critical.