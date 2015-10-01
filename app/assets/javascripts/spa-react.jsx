var mountNode = document.getElementById('bonmots')

var BonMotItem = React.createClass({
    render: function() {
        return (
            // TODO: CSS
            <div className="bonmot-item">
                <div className="bonmot-text">
                    {this.props.item.text}
                </div>
                <div className="bonmot-author">
                    @{this.props.item.username}
                </div>
                <div className="bonmot-timestamp">
                    {this.props.item.createdOn}
                </div>
            </div>
        );
    }
});

var BonMotList = React.createClass({
    getInitialState: function() {
      return { items: [] };
    },
    componentDidMount: function() {

        // TODO: Set state when user clicks the search button

        $.fn.bonmotReact(this);

    },
    render: function() {
        return (
            <div className="bonmot-list">
                {
                    this.state.items.map(function(item) {
                        return <BonMotItem item={item} />
                    })
                }
            </div>
        );
    }
});

React.render(<BonMotList />, mountNode);