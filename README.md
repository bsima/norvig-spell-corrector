# norvig-spell-corrector

See original article here: http://norvig.com/spell-correct.html


## Perf

totally unscientific and not indicative of anything

| Mispelled word| Python         | Clojure       |
| ------------- | -------------- | ------------- |
| speling       | 0.02837 msecs  | 1.4958 msecs  |
| korrectud     | 0.02980 msecs  | 827.78 msecs  |


### Python

```python
>>> import time

>>> def timeit(f):
...     start = time.time()
...     print(f)
...     print("--- {0} seconds ---".format(time.time() - start))

>>> timeit(candidates("speling"))
{'spelling'}
--- 2.8371810913085938e-05 seconds ---

>>> timeit(candidates("korrectud"))
{'corrected'}
--- 2.9802322387695312e-05 seconds ---

```

### Clojure

``` clojure
n.core> (time (correction "korrectud"))
"Elapsed time: 827.784249 msecs"
corrected
n.core> (time (correction "speling"))
"Elapsed time: 1.495869 msecs"
spelling
n.core> 
```


## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
