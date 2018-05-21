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

package com.mongodb.stitch.server.services.aws.ses.internal;

import com.mongodb.stitch.core.services.aws.ses.AwsSesSendResult;
import com.mongodb.stitch.core.services.aws.ses.CoreAwsSesServiceClient;
import com.mongodb.stitch.server.core.services.StitchService;
import com.mongodb.stitch.server.services.aws.ses.AwsSesServiceClient;
import javax.annotation.Nonnull;

public final class AwsSesServiceClientImpl extends CoreAwsSesServiceClient
    implements AwsSesServiceClient {

  public AwsSesServiceClientImpl(final StitchService service) {
    super(service);
  }

  /**
   * Sends an email.
   *
   * @param to The email address to send the email to.
   * @param from The email address to send the email from.
   * @param subject The subject of the email.
   * @param body The body text of the email.
   * @return The result of the send.
   */
  public AwsSesSendResult sendEmail(
      @Nonnull final String to,
      @Nonnull final String from,
      @Nonnull final String subject,
      @Nonnull final String body) {
    return sendEmailInternal(to, from, subject, body);
  }
}