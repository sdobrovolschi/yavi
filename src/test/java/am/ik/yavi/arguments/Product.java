/*
 * Copyright (C) 2018-2020 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package am.ik.yavi.arguments;

import am.ik.yavi.builder.ArgumentsValidatorBuilder;

public class Product {
	private final String name;
	private final int price;

	static final Arguments2Validator<String, Integer, Product> validator = ArgumentsValidatorBuilder
			.of(Product::new) //
			.builder(b -> b //
					._string(Arguments1::arg1, "name", c -> c.notEmpty())
					._integer(Arguments2::arg2, "price", c -> c.greaterThan(0)))
			.build();

	public Product(String name, int price) {
		validator.validateAndThrowIfInvalid(name, price);
		this.name = name;
		this.price = price;
	}

}