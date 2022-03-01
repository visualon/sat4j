package org.sat4j.pb.tools;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CombinationIterator {

    /* contains a combination of indexes ; ascending order */
    // private int indexes[] = null;

    /* contains items ; ascending order */
    private int items[] = null;

    private final int combSize;

    private final int itemsSize;

    // private boolean hasNext = true;

    public CombinationIterator(int combSize, int itemsSize) {
        this.combSize = combSize;
        this.itemsSize = itemsSize;
        this.items = new int[this.itemsSize];
        for (int i = 0; i < this.itemsSize; ++i) {
            this.items[i] = i;
        }
        // computeNext();
    }

    public CombinationIterator(int combSize, Set<Integer> items) {
        this.combSize = combSize;
        this.itemsSize = items.size();
        this.items = new int[this.itemsSize];
        int i = 0;
        for (Integer item : items) {
            this.items[i++] = item;
        }
        // computeNext();
    }

    public CombinationIterator(int combSize, BitSet items) {
        this(combSize, items, 0);
    }

    public CombinationIterator(int combSize, BitSet items, int offset) {
        this.combSize = combSize;
        this.itemsSize = items.cardinality();
        this.items = new int[this.itemsSize];
        int index = 0;
        for (int i = items.nextSetBit(0); i >= 0; i = items.nextSetBit(i + 1)) {
            this.items[index++] = i + offset;
        }
        // computeNext();
    }

    // public Iterator<Set<Integer>> iterator() {
    // return this;
    // }

    public Iterator<Set<Integer>> IntSetIterator() {
        return new CombinationIteratorBase<>(this.items, this.combSize,
                this.itemsSize, HashSet::new);
    }

    public Iterator<BitSet> BitSetIterator() {
        return new CombinationIteratorBase<>(this.items, this.combSize,
                this.itemsSize,
                l -> l.stream().reduce(new BitSet(), (acc, i) -> {
                    acc.set(i);
                    return acc;
                }, (a, b) -> {
                    a.or(b);
                    return a;
                }));
    }

    // public Iterator<Set<Integer>> iterator() {
    //     final int[] superItems = this.items;
    //     final int superCombSize = this.combSize;
    //     final int superItemSize = this.itemsSize;
    //     return new Iterator<Set<Integer>>() {

    //         private int indexes[] = null;

    //         private int items[] = superItems;

    //         private boolean hasNext = true;

    //         private final int combSize = superCombSize;

    //         private final int itemsSize = superItemSize;

    //         public boolean hasNext() {
    //             return this.hasNext;
    //         }

    //         public Set<Integer> next() {
    //             if (this.indexes == null) {
    //                 computeNext();
    //             }
    //             if (!this.hasNext) {
    //                 throw new NoSuchElementException();
    //             }
    //             Set<Integer> nextSet = new HashSet<>();
    //             for (Integer i : this.indexes) {
    //                 nextSet.add(this.items[i]);
    //             }
    //             computeNext();
    //             return nextSet;
    //         }

    //         private void computeNext() {
    //             if (this.indexes == null) {
    //                 this.indexes = new int[this.combSize];
    //                 for (int i = 0; i < this.combSize; ++i) {
    //                     this.indexes[i] = i;
    //                 }
    //                 return;
    //             }
    //             int j;
    //             for (j = this.combSize - 1; j >= 0; --j) {
    //                 ++this.indexes[j];
    //                 if (this.indexes[j] == this.itemsSize - this.combSize + j
    //                         + 1) {
    //                     if (j == 0) {
    //                         this.hasNext = false;
    //                         return;
    //                     }
    //                 } else {
    //                     break;
    //                 }
    //             }
    //             for (int k = j + 1; k < this.combSize; ++k) {
    //                 this.indexes[k] = this.indexes[k - 1] + 1;
    //             }
    //         }

    //     };
    // }

    private class CombinationIteratorBase<T> implements Iterator<T> {
        private int indexes[] = null;

        private final int items[];

        private boolean hasNext = true;

        private final int combSize;

        private final int itemsSize;

        private final Function<List<Integer>, T> nextProvider;

        private CombinationIteratorBase(int items[], int combSize,
                int itemsSize, Function<List<Integer>, T> nextProvider) {
            this.items = items;
            this.combSize = combSize;
            this.itemsSize = itemsSize;
            this.nextProvider = nextProvider;
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public T next() {
            if (this.indexes == null) {
                computeNext();
            }
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            T result = this.nextProvider.apply(Arrays.stream(indexes)
                    .mapToObj(i -> this.items[i]).collect(Collectors.toList()));
            computeNext();
            return result;
        }

        private void computeNext() {
            if (this.indexes == null) {
                this.indexes = new int[this.combSize];
                for (int i = 0; i < this.combSize; ++i) {
                    this.indexes[i] = i;
                }
                return;
            }
            int j;
            for (j = this.combSize - 1; j >= 0; --j) {
                ++this.indexes[j];
                if (this.indexes[j] == this.itemsSize - this.combSize + j + 1) {
                    if (j == 0) {
                        this.hasNext = false;
                        return;
                    }
                } else {
                    break;
                }
            }
            for (int k = j + 1; k < this.combSize; ++k) {
                this.indexes[k] = this.indexes[k - 1] + 1;
            }
        }
    }

    // public BitSet nextBitSet() {
    // BitSet nextSet = new BitSet();
    // for (Integer i : this.indexes) {
    // nextSet.set(this.items[i]);
    // }
    // computeNext();
    // return nextSet;
    // }
}