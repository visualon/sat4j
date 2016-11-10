package org.sat4j.csp.intension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum EOperator {
	
	OPPOSITE("neg", EExpressionType.INTEGER, 1, 1, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			Map<Integer, Integer> ret = new HashMap<>();
			for(Map.Entry<Integer, Integer> entry : mappings.get(0).entrySet()) {
				ret.put(-entry.getKey(), entry.getValue());
			}
			return ret;
		}
	},
	
	ABSOLUTE_VALUE("abs", EExpressionType.INTEGER, 1, 1, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			Map<Integer, Integer> ret = new HashMap<>();
			for(Map.Entry<Integer, Integer> entry : mappings.get(0).entrySet()) {
				ret.put(Math.abs(entry.getKey()), entry.getValue());
			}
			return ret;
		}
	},
	
	ADDITION("add", EExpressionType.INTEGER, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()+entry2.getKey());
				}
			}
			return ret;
		}
	},
	
	SUBTRACTION("sub", EExpressionType.INTEGER, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()-entry2.getKey());
				}
			}
			return ret;
		}
	},
	
	MULTIPLICATION("mul", EExpressionType.INTEGER, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()*entry2.getKey());
				}
			}
			return ret;
		}
	},
	
	INTEGER_DIVISION("div", EExpressionType.INTEGER, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()/entry2.getKey());
				}
			}
			return ret;
		}
	},
	
	REMAINDER("mod", EExpressionType.INTEGER, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()%entry2.getKey());
				}
			}
			return ret;
		}
	},
	
	SQUARE("sqr", EExpressionType.INTEGER, 1, 1, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			Map<Integer, Integer> ret = new HashMap<>();
			for(Map.Entry<Integer, Integer> entry : mappings.get(0).entrySet()) {
				ret.put(entry.getKey()*entry.getKey(), entry.getValue());
			}
			return ret;
		}
	},
	
	POWER("pow", EExpressionType.INTEGER, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), (int)Math.pow(entry1.getKey(),entry2.getKey()));
				}
			}
			return ret;
		}
	},
	
	MINIMUM("min", EExpressionType.INTEGER, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), Math.min(entry1.getKey(),entry2.getKey()));
				}
			}
			return ret;
		}
	},
	
	MAXIMUM("max", EExpressionType.INTEGER, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), Math.max(entry1.getKey(),entry2.getKey()));
				}
			}
			return ret;
		}
	},
	
	DISTANCE("dist", EExpressionType.INTEGER, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), Math.abs(entry1.getKey()-entry2.getKey()));
				}
			}
			return ret;
		}
	},
	
	LESS_THAN("lt", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()<entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	LESS_THAN_OR_EQUAL("le", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()<=entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	GREATER_THAN("gt", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()>entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	GREATER_THAN_OR_EQUAL("ge", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()>=entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	DIFFERENT_FROM("ne", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()!=entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	EQUAL_TO("eq", EExpressionType.BOOLEAN, 1, EOperator.INFINITE_ARITY, EAssociativityState.AND_CHAIN_OF_TWO_WITH_FIRST_COMMON) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()==entry2.getKey()?1:0);
				}
			}
			return ret;
		}
	},
	
	SET("set", EExpressionType.SET, 0, EOperator.INFINITE_ARITY, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			throw new IllegalStateException("this operator must be translated before encoding");
		}
	},
	
	MEMBERSHIP("in", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			throw new IllegalStateException("this operator must be translated before encoding");
		}
	},
	
	LOGICAL_NOT("not", EExpressionType.BOOLEAN, 1, 1, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			Map<Integer, Integer> ret = new HashMap<>();
			for(Map.Entry<Integer, Integer> entry : mappings.get(0).entrySet()) {
				ret.put(entry.getKey()==0 ? 1 : 0, entry.getValue());
			}
			return ret;
		}
	},
	
	LOGICAL_AND("and", EExpressionType.BOOLEAN, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()!=0 && entry2.getKey()!=0?1:0);
				}
			}
			return ret;
		}
	},
	
	LOGICAL_OR("or", EExpressionType.BOOLEAN, 1, EOperator.INFINITE_ARITY, EAssociativityState.ASSOCIATIVE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()!=0 || entry2.getKey()!=0?1:0);
				}
			}
			return ret;
		}
	},
	
	LOGICAL_XOR("xor", EExpressionType.BOOLEAN, 2, EOperator.INFINITE_ARITY, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()!=0 ^ entry2.getKey()!=0?1:0);
				}
			}
			return ret;
		}
	},
	
	LOGICAL_EQUIVALENCE("iff", EExpressionType.BOOLEAN, 1, EOperator.INFINITE_ARITY, EAssociativityState.AND_CHAIN_OF_TWO_WITH_FIRST_COMMON) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), (entry1.getKey()!=0) == (entry2.getKey()!=0)?1:0);
				}
			}
			return ret;
		}
	},
	
	LOGICAL_IMPLICATION("imp", EExpressionType.BOOLEAN, 2, 2, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			if(mappings.size() == 1) return mappings.get(0);
			if(mappings.size() > 2) throw new UnsupportedOperationException("use associativity property to build 2-arity operators");
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry1.getKey()==0 || entry2.getKey()!=0?1:0);
				}
			}
			return ret;
		}
	},
	
	ALTERNATIVE("if", EExpressionType.SAME_AS_CHILDREN, 3, 3, EAssociativityState.NONE) {
		public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
			Map<Integer, Integer> ret = new HashMap<>();
			Iterator<Entry<Integer, Integer>> it1 = mappings.get(0).entrySet().iterator();
			while(it1.hasNext()) {
				Entry<Integer, Integer> entry1 = it1.next();
				Iterator<Entry<Integer, Integer>> it2 = mappings.get(1).entrySet().iterator();
				while(it2.hasNext()) {
					Entry<Integer, Integer> entry2 = it2.next();
					Iterator<Entry<Integer, Integer>> it3 = mappings.get(2).entrySet().iterator();
					while(it3.hasNext()) {
						Entry<Integer, Integer> entry3 = it3.next();
						buildImplVar(solver, ret, entry1.getValue(), entry2.getValue(), entry3.getValue(), entry1.getKey()!=0?entry2.getKey():entry3.getKey());
					}
				}
			}
			return ret;
		}
	};
	
	enum EAssociativityState {
		NONE, ASSOCIATIVE, AND_CHAIN_OF_TWO_WITH_FIRST_COMMON;
	}
	

	public static final int INFINITE_ARITY = Integer.MAX_VALUE;
	
	private final String op;
	private final int minArity;
	private final int maxArity;
	private EExpressionType resultType;
	private final EAssociativityState associativityState;

	private EOperator(final String op, final EExpressionType resultType, final int minArity, final int maxArity, final EAssociativityState associativity) {
		this.op = op;
		this.resultType = resultType;
		this.minArity = minArity;
		this.maxArity = maxArity;
		this.associativityState = associativity;
	}
	
	private static final Map<String, EOperator> strOpCache = new HashMap<>();
	
	public static EOperator operator(final String str) {
		EOperator operator = strOpCache.get(str);
		if(operator != null) return operator;
		for(EOperator op : EOperator.values()) {
			if(op.nameAsString().equals(str)) {
				operator = op;
				break;
			}
		}
		if(operator == null) {
			throw new IllegalArgumentException("\""+str+"\" is not a valid operator");
		}
		strOpCache.put(str, operator);
		return operator;
	}

	public String nameAsString() {
		return op;
	}
	
	public EExpressionType resultType() {
		return this.resultType;
	}

	public int minArity() {
		return minArity;
	}

	public int maxArity() {
		return maxArity;
	}
	
	public EAssociativityState associtivityState() {
		return this.associativityState;
	}
	
	public Map<Integer, Integer> encodeWithTseitin(ICspToSatEncoder solver, List<Map<Integer,Integer>> mappings) {
		throw new UnsupportedOperationException("method not implemented for operator "+this.name());
	}
	
	private static void buildImplVar(ICspToSatEncoder solver, Map<Integer, Integer> mapping, Integer var1, Integer var2,
			int value) {
		if(var1 == null) {
			var1 = var2;
			var2 = null;
		}
		if(var1 == null) {
			mapping.put(value, null);
			return;
		}
		Integer implVar = mapping.get(value);
		if(implVar == null) {
			implVar = solver.newVar();
			mapping.put(value, implVar);
		}
		if(var2 == null) {
			solver.addClause(new int[]{-var1, implVar});
		} else {
			solver.addClause(new int[]{-var1, -var2, implVar});
		}
	}
	
	private static void buildImplVar(ICspToSatEncoder solver, Map<Integer, Integer> mapping, Integer var1, Integer var2,
			Integer var3, int value) {
		Integer implVar = mapping.get(value);
		if(var1 == null) {
			var1 = var2;
			var2 = null;
		}
		if(var1 == null) {
			var1 = var3;
			var3 = null;
		}
		if(var1 == null) {
			mapping.put(value, null);
			return;
		}
		if(var2 == null) {
			var2 = var3;
			var3 = null;
		}
		if(implVar == null) {
			implVar = solver.newVar();
			mapping.put(value, implVar);
		}
		if(var2 == null) {
			solver.addClause(new int[]{-var1, implVar});
		} else {
			if(var3 == null) {
				solver.addClause(new int[]{-var1, -var2, implVar});
			} else {
				solver.addClause(new int[]{-var1, -var3, implVar});
			}
		}
	}

}
