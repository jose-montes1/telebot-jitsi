// @flow
//JMP created file


import { connect, store } from 'react-redux';
import {toState} from '../../../base/redux';

import { AbstractButton } from '../../../base/toolbox';
import type { AbstractButtonProps } from '../../../base/toolbox';
import { translate } from '../../../base/i18n';
import {getCurrentConference } from '../../../base/conference';



/**
 * The type of the React {@code Component} props of {@link AudioOnlyButton}.
 */
type Props = AbstractButtonProps & {

    /**
     * Whether the current conference is in audio only mode or not.
     */
    current_conference: Object,

};

const upCommand = 'L|100:R|100';
const downCommand = 'L|-100:R|-100';
const leftCommand = 'L|-100:R|100';
const rightCommand = 'L|100:R|-100';

const upIcon = 'circle-up';
const downIcon = 'arrow-thick-down';
const leftIcon = 'arrow-thick-left';
const rightIcon = 'arrow-thick-right';

const upLabel = 'upButton';
const downLabel = 'downButton';
const leftLabel = 'leftButton';
const rightLabel = 'rightLabel';

class TelebotCommandButton extends AbstractButton<Props, *>{
	
	TelebotCommandButton(command, iconName, label){
		this.command = command;
		this.iconName = iconName;
		this.label = 'toolbar.' + label;
		this.tooltip = label;
		accesibilityLabel = 'toolbar.accessibilityLabel.' + label;
	}

	/**
     * Handles clicking / pressing the button.
     *
     * @override
     * @protected
     * @returns {void}
     */
	_handleClick(){
		if (this.props.current_conference !== 'undefinded'){
			this.props.current_conference.sendTextMessage(this.command);	
		}
		

	}

}

function _mapStateToProps(state): Object {
	const { conference } = toState(state)['features/base/conference'];
    return {
        current_conference: conference,
    }
}


class UpButton extends TelebotCommandButton {
	command = upCommand;
	iconName = upIcon;
	label = 'toolbar.' + upLabel;
	tooltip = upLabel;
	accesibilityLabel = 'toolbar.accessibilityLabel.' + upLabel;

}

class DownButton extends TelebotCommandButton {
	command = downCommand;
	iconName = downIcon;
	label = 'toolbar.' + downLabel;
	tooltip = downLabel;
	accesibilityLabel = 'toolbar.accessibilityLabel.' + downLabel;

}

class LeftButton extends TelebotCommandButton {
	command = leftCommand;
	iconName = leftIcon;
	label = 'toolbar.' + leftLabel;
	tooltip = leftLabel;
	accesibilityLabel = 'toolbar.accessibilityLabel.' + leftLabel;

}

class RightButton extends TelebotCommandButton {
	command = rightCommand;
	iconName = rightIcon;
	label = 'toolbar.' + rightLabel;
	tooltip = rightLabel;
	accessibilityLabel = 'toolbar.accessibilityLabel.' + rightLabel;
}



export const UpArrowButton = translate(connect(_mapStateToProps)(UpButton));
export const DownArrowButton = translate(connect(_mapStateToProps)(DownButton));
export const LeftArrowButton = translate(connect(_mapStateToProps)(LeftButton));
export const RightArrowButton = translate(connect(_mapStateToProps)(RightButton));
