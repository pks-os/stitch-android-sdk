/*
 * Copyright 2018-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.stitch.core.services.aws.ses.internal;

import static com.mongodb.stitch.core.testutils.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.mongodb.stitch.core.services.aws.ses.AwsSesSendResult;
import com.mongodb.stitch.core.services.internal.CoreStitchServiceClient;

import java.util.List;
import org.bson.Document;
import org.bson.codecs.Decoder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CoreAwsSesServiceClientUnitTests {

  @Test
  @SuppressWarnings({"unchecked", "deprecation"})
  public void testSendMessage() {
    final CoreStitchServiceClient service = Mockito.mock(CoreStitchServiceClient.class);
    final CoreAwsSesServiceClient client = new CoreAwsSesServiceClient(service);

    final String to = "eliot@10gen.com";
    final String from = "dwight@10gen.com";
    final String subject = "Hello";
    final String body = "again friend";

    final String expectedMessageId = "yourMessageId";


    Mockito.doReturn(new AwsSesSendResult(expectedMessageId))
        .when(service).callFunction(any(), any(), any(Decoder.class));

    final AwsSesSendResult result = client.sendEmail(to, from, subject, body);
    assertEquals(result.getMessageId(), expectedMessageId);

    final ArgumentCaptor<String> funcNameArg = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<List> funcArgsArg = ArgumentCaptor.forClass(List.class);
    final ArgumentCaptor<Decoder<AwsSesSendResult>> resultClassArg =
        ArgumentCaptor.forClass(Decoder.class);
    verify(service)
        .callFunction(
            funcNameArg.capture(),
            funcArgsArg.capture(),
            resultClassArg.capture());

    assertEquals("send", funcNameArg.getValue());
    assertEquals(1, funcArgsArg.getValue().size());
    final Document expectedArgs = new Document();
    expectedArgs.put("toAddress", to);
    expectedArgs.put("fromAddress", from);
    expectedArgs.put("subject", subject);
    expectedArgs.put("body", body);
    assertEquals(expectedArgs, funcArgsArg.getValue().get(0));
    assertEquals(ResultDecoders.sendResultDecoder, resultClassArg.getValue());

    // Should pass along errors
    doThrow(new IllegalArgumentException("whoops"))
        .when(service).callFunction(any(), any(), any(Decoder.class));
    assertThrows(() -> client.sendEmail(to, from, subject, body),
        IllegalArgumentException.class);
  }
}
