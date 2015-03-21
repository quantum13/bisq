/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.trade.protocol.trade.taker.tasks;

import io.bitsquare.common.taskrunner.Task;
import io.bitsquare.common.taskrunner.TaskRunner;
import io.bitsquare.trade.protocol.trade.messages.RequestTakerDepositPaymentMessage;
import io.bitsquare.trade.protocol.trade.taker.models.TakerAsSellerModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import static io.bitsquare.util.Validator.*;

public class ProcessRequestTakerDepositPaymentMessage extends Task<TakerAsSellerModel> {
    private static final Logger log = LoggerFactory.getLogger(ProcessRequestTakerDepositPaymentMessage.class);

    public ProcessRequestTakerDepositPaymentMessage(TaskRunner taskHandler, TakerAsSellerModel model) {
        super(taskHandler, model);
    }

    @Override
    protected void doRun() {
        try {
            checkTradeId(model.id, model.getTradeMessage());
            RequestTakerDepositPaymentMessage message = (RequestTakerDepositPaymentMessage) model.getTradeMessage();

            model.offerer.connectedOutputsForAllInputs = checkNotNull(message.offererConnectedOutputsForAllInputs);
            checkArgument(message.offererConnectedOutputsForAllInputs.size() > 0);
            model.offerer.outputs = checkNotNull(message.offererOutputs);
            model.offerer.tradeWalletPubKey = checkNotNull(message.offererTradeWalletPubKey);
            model.offerer.p2pSigPublicKey = checkNotNull(message.offererP2PSigPublicKey);
            model.offerer.p2pEncryptPubKey = checkNotNull(message.offererP2PEncryptPublicKey);
            model.offerer.fiatAccount = checkNotNull(message.offererFiatAccount);
            model.offerer.accountId = nonEmptyStringOf(message.offererAccountId);

            complete();
        } catch (Throwable t) {
            failed(t);
        }
    }
}