/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package temp.spring.core;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * Simple pass-through encoder for {@link DataBuffer}s.
 *
 * @author Arjen Poutsma
 * @since 5.0
 */
public class DataBufferEncoder extends AbstractEncoder<DataBuffer> {

	public DataBufferEncoder() {
		super(MimeTypeUtils.ALL);
	}


	@Override
	public boolean canEncode(ResolvableType elementType, MimeType mimeType) {
		Class<?> clazz = elementType.getRawClass();
		return (super.canEncode(elementType, mimeType) && DataBuffer.class.isAssignableFrom(clazz));
	}

	@Override
	public Flux<DataBuffer> encode(Publisher<? extends DataBuffer> inputStream,
                                   DataBufferFactory bufferFactory, ResolvableType elementType, MimeType mimeType,
                                   Map<String, Object> hints) {

		return Flux.from(inputStream);
	}

}
